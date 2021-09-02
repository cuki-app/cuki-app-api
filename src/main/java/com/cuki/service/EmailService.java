package com.cuki.service;

import com.cuki.controller.dto.VerificationCodeRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.mail.Message;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Random;

@Slf4j
@RequiredArgsConstructor
@Service
public class EmailService {

    private final JavaMailSender mailSender;
    public static String verificationCode;

    private MimeMessage createMessage(VerificationCodeRequestDto verificationCodeRequestDto) throws Exception {

        String to = verificationCodeRequestDto.getEmail();
        String purpose = verificationCodeRequestDto.getPurpose().toString();

        // 인증번호 생성
        verificationCode = createVerificationCode();

        log.info("보내는 대상 : " + to);
        log.info("인증 번호 : " + verificationCode);
        MimeMessage message = mailSender.createMimeMessage();

        message.addRecipients(Message.RecipientType.TO, to); // 보내는 대상
        message.setSubject("[cuki] 인증번호를 확인해주세요.");  // 제목

        // purpose 값에 해당하는 내용 생성
        String html = null;
        if (purpose.equals("SIGN_UP")) html = createHtmlForSignUp();
        else if (purpose.equals("LOGIN")) html = createHtmlForLogin();
        else throw new RuntimeException("purpose can't be null");

        message.setText(html, "utf-8", "html");  // 내용
        message.setFrom(new InternetAddress("verycona@gmail.com", "cuki")); // 보내는 사람

        return message;
    }

    // 인증코드 만들기
    private static String createVerificationCode() {
        StringBuilder verificationCode = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < 8; i++) {   // 인증코드 8자리
            verificationCode.append((random.nextInt(10)));
        }
        return verificationCode.toString();
    }

    public void sendVerificationCode(VerificationCodeRequestDto verificationCodeRequestDto) throws Exception {
        MimeMessage message = createMessage(verificationCodeRequestDto);
        try {
            mailSender.send(message);
        } catch (MailException e){
            e.printStackTrace();
            throw new IllegalArgumentException();
        }
    }

    private String createHtmlForLogin() {
        String html = "";
        html += "<div align='center' style='margin:50px;'>";
        html += "<h1>로그인 안내</h1>";
        html += "<br>";
        html += "<p>아래 인증 코드를 입력하시면 로그인이 완료됩니다. 감사합니다.</p>";
        html += "<br>";
        html+= "<div align='center' style='border:1px solid black; font-family:verdana';>";
        html+= "<h3 style='color:darkgreen;'>로그인 코드</h3>";
        html+= "<div style='font-size:130%'>";
        html+= "CODE : <strong>";
        html+= verificationCode;
        html+= "</strong><div><br/> ";
        html += "<a href='https://cuki.io' target='_blank'>cuki 홈페이지 바로가기</a>";
        html+= "</div>";

        return html;
    }

    private String createHtmlForSignUp() {
        String html = "";
        html += "<div align='center' style='margin:50px;'>";
        html += "<h1>가입 안내</h1>";
        html += "<br>";
        html += "<p>아래 인증 코드를 입력하시면 회원가입이 완료됩니다. 감사합니다.</p>";
        html += "<br>";
        html+= "<div align='center' style='border:1px solid black; font-family:verdana';>";
        html+= "<h3 style='color:darkgreen;'>회원가입 코드</h3>";
        html+= "<div style='font-size:130%'>";
        html+= "CODE : <strong>";
        html+= verificationCode;
        html+= "</strong><div><br/> ";
        html += "<a href='https://cuki.io' target='_blank'>cuki 홈페이지 바로가기</a>";
        html+= "</div>";

        return html;
    }

}
