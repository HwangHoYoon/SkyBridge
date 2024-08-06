package com.skybridge.teacher.service;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.LoadState;
import com.skybridge.teacher.entity.TeacherInfo;
import com.skybridge.teacher.repository.TeacherInfoRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class TeacherService {
    private final TeacherInfoRepository teacherInfoRepository;

    @Transactional
    public void teacher() {
        List<TeacherInfo> teacherInfoList = new ArrayList<>();

        try (Playwright playwright = Playwright.create()) {
            // 브라우저 런처 생성
            BrowserType.LaunchOptions options = new BrowserType.LaunchOptions();
            options.setHeadless(false);  // 브라우저가 보이도록 설정

            // 브라우저 시작 (Chromium, Firefox, WebKit 중 선택)
            Browser browser = playwright.chromium().launch(options);

            // 새로운 브라우저 컨텍스트와 페이지 생성
            BrowserContext context = browser.newContext();
            Page page = context.newPage();

            Map<String, String> urlList = getUrlList();

            // URL로 이동
            urlList.forEach((key, url) -> {
                page.navigate(url);

                // 페이지 이동을 위한 무한 루프 마지막 페이지로 이동하면 멈춘다.
                boolean reachedEnd = false;
                while (!reachedEnd) {

                    // 특정 태그 값 가져오기
                    // 특정 클래스 값을 가진 ul 안의 li 요소의 텍스트 내용 가져오기
                    List<ElementHandle> liElements = page.locator(".thumb_list > li").elementHandles();

                    for (ElementHandle liElement : liElements) {
                        ElementHandle titA = liElement.querySelector(".tit > a");
                        String titUrl = titA.getAttribute("href");

                        Page newPage = context.newPage();
                        String subUrl = "https://www.ebsi.co.kr" + titUrl;
                        newPage.navigate(subUrl);
                        newPage.waitForLoadState(LoadState.LOAD);
                        newPage.waitForLoadState(LoadState.DOMCONTENTLOADED);
                        newPage.waitForLoadState(LoadState.NETWORKIDLE);

                        TeacherInfo teacherInfo = new TeacherInfo();
                        teacherInfo.setType(key);
                        teacherInfo.setRegDate(LocalDate.now());
                        teacherInfo.setUrl(subUrl);

                        ElementHandle titH2 = newPage.querySelector(".tit_wrap > h2");
                        if (titH2 != null) {
                            String courseName = getTextContent(titH2);
                            teacherInfo.setCourseName(courseName);
                        }


                        ElementHandle nameSt = newPage.querySelector(".name > strong");
                        if (nameSt != null) {
                            String teacherName = getTextContent(nameSt);
                            teacherInfo.setTeacherName(teacherName);
                        }

                        List<ElementHandle> divElements = newPage.locator(".view_content > div").elementHandles();
                        for (ElementHandle divElement : divElements) {
                            ElementHandle contTit = divElement.querySelector(".cont_tit");
                            if (contTit != null) {
                                String contTitName = getTextContent(contTit);
                                if (StringUtils.equals(contTitName, "강좌범위")) {
                                    ElementHandle contPara = divElement.querySelector(".cont_para");
                                    if (contPara != null) {
                                        String contParaName = getTextContent(contPara);
                                        teacherInfo.setCourseScope(contParaName);
                                    }
                                } else if (StringUtils.equals(contTitName, "강좌특징")) {
                                    ElementHandle contPara = divElement.querySelector(".cont_para");
                                    if (contPara != null) {
                                        String contParaName = getTextContent(contPara);
                                        teacherInfo.setCourseFeatures(contParaName);
                                    }
                                } else if (StringUtils.equals(contTitName, "추천대상")) {
                                    ElementHandle contPara = divElement.querySelector(".cont_para");
                                    if (contPara != null) {
                                        String contParaName = getTextContent(contPara);
                                        teacherInfo.setTargetAudience(contParaName);
                                    }
                                } else if (StringUtils.equals(contTitName, "강좌정보")) {
                                    List<ElementHandle> dtElements = newPage.locator(".cont_info2 > dt").elementHandles();

                                    for (ElementHandle dtElement : dtElements) {
                                        ElementHandle span = dtElement.querySelector("span");
                                        if (span != null) {
                                            String spanName = getTextContent(span);
                                            ElementHandle ddElement = dtElement.evaluateHandle("dt => dt.nextElementSibling").asElement();
                                            if (ddElement != null) {
                                                String textContent = getTextContent(ddElement);
                                                if (StringUtils.equals(spanName, "과목")) {
                                                    teacherInfo.setSubject(textContent);
                                                } else if (StringUtils.equals(spanName, "학습단계")) {
                                                    teacherInfo.setLearningStage(textContent);
                                                } else if (StringUtils.equals(spanName, "수준")) {
                                                    teacherInfo.setLevel(textContent);
                                                } else if (StringUtils.equals(spanName, "학년")) {
                                                    teacherInfo.setGrade(textContent);
                                                } else if (StringUtils.equals(spanName, "제작 강수")) {
                                                    teacherInfo.setLectureCount(textContent);
                                                } else if (StringUtils.equals(spanName, "교재")) {
                                                    teacherInfo.setTextbook(textContent);
                                                } else if (StringUtils.equals(spanName, "강좌 관심수")) {
                                                    teacherInfo.setCourseInterestCount(textContent);
                                                } else if (StringUtils.equals(spanName, "평균별점")) {
                                                    teacherInfo.setAverageRating(textContent);
                                                } else if (StringUtils.equals(spanName, "관련시리즈")) {
                                                    teacherInfo.setRelatedSeries(textContent);
                                                }
                                            }
                                        }
                                    }
                                } else if (StringUtils.equals(contTitName, "학습자 분포도")) {
                                    List<ElementHandle> memberElementHandles = newPage.locator(".member_interest > div").elementHandles();

                                    for (ElementHandle memberElementHandle : memberElementHandles) {
                                        String gradle = getTextContent(memberElementHandle);
                                        ElementHandle span = memberElementHandle.querySelector("span");
                                        if (span != null) {
                                            String spanText = getTextContent(span).replace("(", "").replace(")", "");
                                            if (StringUtils.contains(gradle, "고3")) {
                                                teacherInfo.setSeniorYear(spanText);
                                            } else if (StringUtils.contains(gradle, "고2")) {
                                                teacherInfo.setJuniorYear(spanText);
                                            } else if (StringUtils.contains(gradle, "고1")) {
                                                teacherInfo.setSophomoreYear(spanText);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        teacherInfoList.add(teacherInfo);
                        // 새 창 닫기
                        newPage.close();
                    }
                    // 다음페이지 버튼으 눌리지 않는다면 마지막 페이지로 간주
                    // a 태그의 style 속성에서 cursor라는 단어가 있는지 체크
                    String styleValue = page.getAttribute("a.btn_next", "style");
                    if (styleValue != null && styleValue.contains("cursor")) {
                        reachedEnd = true;
                    } else {
                        // 그렇지 않다면 다음 페이지 클릭
                        page.click("a.btn_next");
                        // 로딩 다되는지 확인
                        page.waitForLoadState(LoadState.LOAD);
                        page.waitForLoadState(LoadState.DOMCONTENTLOADED);
                        page.waitForLoadState(LoadState.NETWORKIDLE);
                    }
                }
            });
            // 브라우저 종료
            browser.close();
        }
        if (!teacherInfoList.isEmpty()) {
            teacherInfoRepository.saveAll(teacherInfoList);
        }
    }

    private Map<String, String> getUrlList() {
        Map<String, String> urlList = new HashMap<>();

        String kor = "https://www.ebsi.co.kr/ebs/pot/potn/retrieveSbjtListByArea.ebs?categoryCode=A100&cookieGradeVal=high3";
        String math = "https://www.ebsi.co.kr/ebs/pot/potn/retrieveSbjtListByArea.ebs?categoryCode=A300&cookieGradeVal=high3";
        String eng = "https://www.ebsi.co.kr/ebs/pot/potn/retrieveSbjtListByArea.ebs?categoryCode=A200&cookieGradeVal=high3";
        String society = "https://www.ebsi.co.kr/ebs/pot/potn/retrieveSbjtListByArea.ebs?categoryCode=A500&cookieGradeVal=high3";
        String science = "https://www.ebsi.co.kr/ebs/pot/potn/retrieveSbjtListByArea.ebs?categoryCode=A400&cookieGradeVal=high3";

        urlList.put("kor", kor);
        urlList.put("math", math);
        urlList.put("eng", eng);
        urlList.put("society", society) ;
        urlList.put("science", science);
        return urlList;
    }

    private String getTextContent(ElementHandle elementHandle) {
        return elementHandle.textContent().trim().replace("¶", "");
    }
}
