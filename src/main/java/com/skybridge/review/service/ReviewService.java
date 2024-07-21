package com.skybridge.review.service;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.LoadState;
import com.skybridge.review.entity.Review;
import com.skybridge.review.entity.ReviewDetail;
import com.skybridge.review.repository.ReviewDetailRepository;
import com.skybridge.review.repository.ReviewRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final ReviewDetailRepository reviewDetailRepository;

    @Transactional
    public void review() {

        List<Review> reviewList = new ArrayList<>();
        List<ReviewDetail> reviewDetailList = new ArrayList<>();

        // Playwright 객체 생성
        try (Playwright playwright = Playwright.create()) {
            // 브라우저 런처 생성
            BrowserType.LaunchOptions options = new BrowserType.LaunchOptions();
            options.setHeadless(false);  // 브라우저가 보이도록 설정

            // 브라우저 시작 (Chromium, Firefox, WebKit 중 선택)
            Browser browser = playwright.chromium().launch(options);

            // 새로운 브라우저 컨텍스트와 페이지 생성
            BrowserContext context = browser.newContext();
            Page page = context.newPage();

            // URL로 이동
            page.navigate("https://www.jinhak.com/Lecture/Review/Survey/TeacherSurvey.aspx#4");


            // 인피니티 스크롤을 처리하기 위한 루프
            boolean reachedEnd = false;
            while (!reachedEnd) {
                // 현재 스크롤 높이 저장
                Integer previousHeight = (Integer) page.evaluate("document.body.scrollHeight");

                // 페이지 끝까지 스크롤
                page.evaluate("window.scrollTo(0, document.body.scrollHeight)");

                // 스크롤이 완료될 시간을 확보
                page.waitForTimeout(2000);

                // 새로운 스크롤 높이 저장
                Integer currentHeight = (Integer) page.evaluate("document.body.scrollHeight");

                // 스크롤 높이에 변화가 없다면 마지막까지 스크롤한 것으로 간주
                reachedEnd = previousHeight.equals(currentHeight);
            }

            // 특정 태그 값 가져오기
            // 특정 클래스 값을 가진 ul 안의 li 요소의 텍스트 내용 가져오기
            List<ElementHandle> liElements = page.locator("#lvReviewList > li").elementHandles();

            for (ElementHandle liElement : liElements) {
                String sWorkNameText = "";
                String sSubjectNameText = "";
                String iTotalScoreText = "";
                String rBadText = "";
                String rGoodText = "";
                String TagListText = "";

                // li 요소 안에서 특정 클래스 값을 가진 자식 요소 찾기
                ElementHandle sWorkName = liElement.querySelector(".sWorkName");

                if (sWorkName != null) {
                    sWorkNameText = sWorkName.textContent();
                }

                ElementHandle sSubjectName = liElement.querySelector(".sSubjectName");
                if (sSubjectName != null) {
                    sSubjectNameText = sSubjectName.textContent();
                }

                ElementHandle iTotalScore = liElement.querySelector(".iTotalScore");
                if (iTotalScore != null) {
                    iTotalScoreText = iTotalScore.textContent();
                }

                ElementHandle rBad = liElement.querySelector(".rBad");
                if (rBad != null) {
                    rBadText = rBad.textContent();
                }

                ElementHandle rGood = liElement.querySelector(".rGood");
                if (rGood != null) {
                    rGoodText = rGood.textContent();
                }

                ElementHandle TagList = liElement.querySelector(".TagList");
                if (TagList != null) {
                    TagListText = TagList.textContent();
                }

                Review review = new Review();
                review.setName(sWorkNameText);
                review.setSubject(sSubjectNameText);
                review.setScore(iTotalScoreText);
                review.setBad(rBadText);
                review.setGood(rGoodText);
                review.setTag(TagListText);
                review.setRegDate(LocalDate.now());
                reviewList.add(review);

                // 각 LI를 클릭하여 새 창 열기
                //Page newPage = browser.newContext().waitForPage(liElement::click);

                // 새 창이 로드될 때까지 기다리기
               // newPage.waitForLoadState(LoadState.LOAD);

                // 특정 요소를 클릭하여 이동할 URL을 추출합니다
                // 일단 페이지 네비게이션이 발생할 때까지 기다립니다.
                //Page pageTwo = context.newPage();
/*                Page newPage = context.waitForPage(() -> {
                   // liElement.click();
                    //page.click(liElement.click());
                    liElement.evaluate("element => element.click({button: 'left', modifiers: ['Shift']})");
                    //liElement.evaluate("el => el.click()");
                });*/

                //liElement.evaluate("() => { return document.querySelector('.iMove').onclick.toString(); }");

                String onclickCode =  (String) liElement.querySelector(".iMove").evaluate("element => element.onclick ? element.onclick.toString() : 'No onclick handler'");

                // 정규 표현식: goDetail('460')에서 숫자 값만 추출
                Pattern pattern = Pattern.compile("goDetail\\('(\\d+)'\\)");
                Matcher matcher = pattern.matcher(onclickCode);

                if (matcher.find()) {
                    String code = matcher.group(1);

                    Page newPage = context.newPage();
                    newPage.navigate("https://www.jinhak.com/Lecture/Review/MiniHome/TeacherMiniHome.aspx?TeacherCode=" + code);
                    newPage.waitForLoadState(LoadState.LOAD);
                    newPage.waitForLoadState(LoadState.DOMCONTENTLOADED);
                    newPage.waitForLoadState(LoadState.NETWORKIDLE);

                    // 인피니티 스크롤을 처리하기 위한 루프
                    boolean subReachedEnd = false;
                    while (!subReachedEnd) {
                        // 현재 스크롤 높이 저장
                        Integer previousHeight = (Integer) newPage.evaluate("document.body.scrollHeight");

                        // 페이지 끝까지 스크롤
                        newPage.evaluate("window.scrollTo(0, document.body.scrollHeight)");

                        // 스크롤이 완료될 시간을 확보
                        newPage.waitForTimeout(2000);

                        // 새로운 스크롤 높이 저장
                        Integer currentHeight = (Integer) newPage.evaluate("document.body.scrollHeight");

                        // 스크롤 높이에 변화가 없다면 마지막까지 스크롤한 것으로 간주
                        subReachedEnd = previousHeight.equals(currentHeight);
                    }

                    // 새 창에서 SPAN 태그 값을 추출
                    //List<String> spanValues = extractSpanValues(newPage);
                    List<ElementHandle> ulElements = newPage.querySelectorAll("#lvReviewList > li");
                    // 각 LI에서 SPAN 태그 값을 추출
                    for (ElementHandle li : ulElements) {
                        String spanJobNameText = "";
                        String spanMajorNameText = "";
                        String spanCalcScoreNameText = "";
                        String spanRBadText = "";
                        String spanRGoodText = "";
                        String spanTagListText = "";

                        ElementHandle spanJobName = li.querySelector(".spanJobName");
                        if (spanJobName != null) {
                            spanJobNameText = spanJobName.textContent();
                        }

                        ElementHandle spanMajorName = li.querySelector(".spanMajorName");
                        if (spanMajorName != null) {
                            spanMajorNameText = spanMajorName.textContent();
                        }

                        ElementHandle spanCalcScore = li.querySelector(".spanCalcScore");
                        if (spanCalcScore != null) {
                            spanCalcScoreNameText = spanCalcScore.textContent();
                        }

                        ElementHandle spanRBad = li.querySelector(".rBad");
                        if (spanRBad != null) {
                            spanRBadText = spanRBad.textContent();
                        }

                        ElementHandle spanRGood = li.querySelector("rGood");
                        if (spanRGood != null) {
                            spanRGoodText = spanRGood.textContent();
                        }

                        ElementHandle spanTagList = li.querySelector("TagList");
                        if (spanTagList != null) {
                            spanTagListText = spanTagList.textContent();
                        }

                        ReviewDetail reviewDetail = new ReviewDetail();
                        reviewDetail.setJob(spanJobNameText);
                        reviewDetail.setMajor(spanMajorNameText);
                        reviewDetail.setScore(spanCalcScoreNameText);
                        reviewDetail.setBad(spanRBadText);
                        reviewDetail.setGood(spanRGoodText);
                        reviewDetail.setTag(spanTagListText);
                        reviewDetail.setRegDate(LocalDate.now());
                        reviewDetail.setReview(review);
                        reviewDetailList.add(reviewDetail);
                    }
                    // 새 창 닫기
                    newPage.close();
                }
            }
            // 스크린샷 찍기
            //page.screenshot(new Page.ScreenshotOptions().setPath(Paths.get("screenshot.png")));
            // 브라우저 종료
            browser.close();
        }
        if (!reviewList.isEmpty()) {
            reviewRepository.saveAll(reviewList);
        }

        if (!reviewDetailList.isEmpty()) {
            reviewDetailRepository.saveAll(reviewDetailList);
        }
    }
}
