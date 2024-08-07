package com.skybridge.news.service;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.LoadState;
import com.skybridge.common.exception.CommonErrorCode;
import com.skybridge.common.exception.CommonException;
import com.skybridge.news.dto.NewsRes;
import com.skybridge.news.entity.AdmNew;
import com.skybridge.news.repository.AdmNewRepository;
import com.skybridge.review.entity.Review;
import com.skybridge.teacher.entity.TeacherInfo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Slf4j
public class NewsService {
    private final AdmNewRepository admNewRepository;

    @Value("${domain}")
    private String domain;

    @Value("${crawling.news.url}")
    private String newsUrl;

    @Value("${file.upload.news.path}")
    private String uploadPath;

    public ResponseEntity<List<NewsRes>> news(String sort) {
        List<NewsRes> newsResList = new ArrayList<>();
        Sort sortColumn;
        if (StringUtils.equals("1", sort)) {
            sortColumn = Sort.by("regDate").descending();
        } else {
            sortColumn = Sort.by("viewCount").descending();
        }
        List<AdmNew> admNewList = admNewRepository.findAll(sortColumn);

        if (!admNewList.isEmpty()) {
            admNewList.forEach(admNew -> {
                LocalDate currentDate = admNew.getRegDate();
                String regDateText = currentDate.getYear() + "." + currentDate.getMonthValue() + "." + currentDate.getDayOfMonth();
                NewsRes newsRes = new NewsRes();
                newsRes.setId(admNew.getId());
                newsRes.setNewsName(admNew.getNewsName());
                newsRes.setNewsLink(admNew.getNewsLink());
                newsRes.setNewsType(admNew.getNewsType());
                /*try {
                    newsRes.setNewsImage(encodeImageToBase64(admNew.getNewsImage()));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }*/

                newsRes.setNewsImage(domain + "news/" +"image/" + admNew.getId());

                newsRes.setRegDate(currentDate);
                newsRes.setRegDateText(regDateText);
                newsRes.setViewCount(admNew.getViewCount());
                newsResList.add(newsRes);
            });
        }

        return ResponseEntity.ok(newsResList);
    }

    public String encodeImageToBase64(String imagePath) throws IOException {
        //Resource resource = new ClassPathResource(imagePath);
        byte[] imageBytes = Files.readAllBytes(Paths.get(imagePath));
        return Base64.getEncoder().encodeToString(imageBytes);
    }

    public ResponseEntity<Resource> loadImage(String id) {
        if (StringUtils.isBlank(id)) {
            log.error("선생님 이미지 조회 정보가 잘못되었습니다. {}", id);
            throw new CommonException(CommonErrorCode.INVALID_ID);
        }

        Long resultId = Long.parseLong(id);
        Optional<AdmNew> admNew = admNewRepository.findById(resultId);

        if (admNew.isPresent()) {
            String newsImage = admNew.get().getNewsImage();
            Resource resource = new FileSystemResource(newsImage);
            if (!resource.exists())
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            HttpHeaders header = new HttpHeaders();
            Path filePath = null;
            try {
                filePath = Paths.get(newsImage);
                header.add("Content-type", Files.probeContentType(filePath));
            } catch(IOException e) {
                log.error("이미지를 불러오는대 실패하였습니다. {}", newsImage);
            }
            return new ResponseEntity<>(resource, header, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Transactional
    public void newsScheduled() {
        List<AdmNew> admNewList = new ArrayList<>();

        try (Playwright playwright = Playwright.create()) {
            // 브라우저 런처 생성
            BrowserType.LaunchOptions options = new BrowserType.LaunchOptions();
            options.setHeadless(false);  // 브라우저가 보이도록 설정

            // 브라우저 시작 (Chromium, Firefox, WebKit 중 선택)
            Browser browser = playwright.chromium().launch(options);

            // 새로운 브라우저 컨텍스트와 페이지 생성
            BrowserContext context = browser.newContext();
            Page page = context.newPage();

            String mainUrl = newsUrl;

            // URL로 이동
            page.navigate(mainUrl);

            // 특정 태그 값 가져오기
            // 특정 클래스 값을 가진 ul 안의 li 요소의 텍스트 내용 가져오기
            List<ElementHandle> liElements = page.locator(".lst_type_table > tbody > tr").elementHandles();

            int totCnt = 0;
            for (ElementHandle liElement : liElements) {
                if (totCnt >= 10) {
                    break;
                }

                AdmNew admNew = new AdmNew();

                ElementHandle tdSpan = liElement.querySelector(".td_lft > span");

                ElementHandle tdDate = liElement.querySelector(".td_lft").evaluateHandle("td => td.nextElementSibling").asElement();
                if (tdDate != null) {
                    String tdDateText = tdDate.textContent();
                    admNew.setRegDate(LocalDate.parse(tdDateText));
                    ElementHandle tdCnt = tdDate.evaluateHandle("td => td.nextElementSibling").asElement();

                    if (tdCnt != null) {
                        String tdCntText = tdCnt.textContent();
                        if (StringUtils.isNotBlank(tdCntText)) {
                            admNew.setViewCount(Long.parseLong(tdCntText.replace(",", "")));
                        }
                    }
                }

                if (tdSpan != null) {
                    String tdSpanText = tdSpan.textContent();
                    if (tdSpanText != null) {
                        tdSpanText = tdSpanText.replace("[", "").replace("]", "");

                        if (tdSpanText.equals("수시") || tdSpanText.equals("정시")) {
                            admNew.setNewsType(tdSpanText);

                            ElementHandle tdA = liElement.querySelector(".td_lft > a");

                            String onclickCode =  (String) tdA.evaluate("element => element.onclick ? element.onclick.toString() : 'No onclick handler'");
                            // 정규 표현식: fncNewsView('460')에서 숫자 값만 추출
                            Pattern pattern = Pattern.compile("fncNewsView\\((\\d+)\\)");
                            Matcher matcher = pattern.matcher(onclickCode);

                            if (matcher.find()) {
                                String code = matcher.group(1);
                                long codeCnt = admNewRepository.countByCode(code);

                                if (codeCnt == 0) {
                                    admNew.setCode(code);

                                    Page newPage = context.newPage();
                                    String url = newsUrl + "#news_view_ax.asp?idx=" + code;
                                    admNew.setNewsLink(url);
                                    newPage.navigate(url);
                                    newPage.waitForLoadState(LoadState.LOAD);
                                    newPage.waitForLoadState(LoadState.DOMCONTENTLOADED);
                                    newPage.waitForLoadState(LoadState.NETWORKIDLE);

                                    Locator h2Locator = newPage.locator("h2.commonBoardView--subject");
                                    Locator spanLocator = h2Locator.locator("span");

                                    // span 요소의 텍스트 가져오기
                                    String spanText = spanLocator.textContent();

                                    // h2 요소의 전체 텍스트 가져오기
                                    String h2Text = h2Locator.textContent();

                                    // span 텍스트를 제외한 나머지 텍스트 추출
                                    String textWithoutSpan = h2Text != null ? h2Text.replace(spanText != null ? spanText : "", "").trim() : "";

                                    admNew.setNewsName(textWithoutSpan);

                                    ElementHandle viewContents = newPage.querySelector("div > .viewContents");

                                    if (viewContents != null) {
                                        ElementHandle img = viewContents.querySelector("img");
                                        if (img != null) {
                                            String src = img.getAttribute("src");
                                            String extension = getFileExtension(src);
                                            // 이미지 다운로드
                                            String newsImage = downloadImage(src, File.separator + code + "." + extension);
                                            admNew.setNewsImage(newsImage);
                                        }
                                    }
                                    admNew.setBatDate(LocalDate.now());
                                    admNewList.add(admNew);
                                    // 새 창 닫기
                                    newPage.close();
                                }
                            }
                        }
                    }
                }
                totCnt++;
            }
            // 브라우저 종료
            browser.close();
        }
        if (!admNewList.isEmpty()) {
            // 모든 엔티티의 코드 수집
            Set<String> codes = admNewList.stream()
                    .map(AdmNew::getCode) // Entity에서 코드 추출
                    .collect(Collectors.toSet());

            // 데이터베이스에서 존재하는 코드 조회
            List<AdmNew> existingEntities = admNewRepository.findByCodeIn(codes);
            Set<String> existingCodes = existingEntities.stream()
                    .map(AdmNew::getCode)
                    .collect(Collectors.toSet());

            // 새로 추가할 엔티티만 필터링
            List<AdmNew> newEntities = admNewList.stream()
                    .filter(e -> !existingCodes.contains(e.getCode()))
                    .collect(Collectors.toList());

            // 새로 추가할 엔티티를 저장
            admNewRepository.saveAll(newEntities);
        }
    }

    public String downloadImage(String imageUrl, String destinationFile) {
        try {
            // URL 객체 생성
            URL url = new URL(imageUrl);

            String folderPath = makeFolder();

            String fileName = uploadPath + File.separator + folderPath + destinationFile;
            // URL에서 파일로 복사
            Files.copy(url.openStream(), Paths.get(fileName), StandardCopyOption.REPLACE_EXISTING);

            return fileName;
        } catch (IOException e) {
            log.error(e.getMessage());
            return "";
        }
    }

    public  String getFileExtension(String filename) {
        if (filename == null || filename.isEmpty()) {
            return "";
        }

        int lastIndex = filename.lastIndexOf('.');
        if (lastIndex == -1 || lastIndex == filename.length() - 1) {
            // 파일 이름에 점이 없거나 점이 마지막에 위치하는 경우
            return "";
        }

        return filename.substring(lastIndex + 1);
    }

    private String makeFolder() {

        String str = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));

        String folderPath = str.replace("/", File.separator);

        // make folder --------
        File uploadPathFolder = new File(uploadPath, folderPath);

        if(!uploadPathFolder.exists()) {
            boolean mkdirs = uploadPathFolder.mkdirs();
            log.info("-------------------makeFolder------------------");
            log.info("uploadPathFolder.exists() : {}", uploadPathFolder.exists());
            log.info("mkdirs : {}", mkdirs);
        }
        return folderPath;

    }
}
