package com.cuki.infra.email;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.mail.Message;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.time.Duration;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Random;

@Slf4j
@Service
public class EmailService {

    private final JavaMailSender mailSender;
    private final RedisTemplate<String, Object> redisTemplate;

    private final String KEY_PREFIX = "email:";
    private final int LIMIT_TIME = 60 * 5;
    private String verificationCode;

    public EmailService(JavaMailSender mailSender, RedisTemplate<String, Object> redisTemplate) {
        this.mailSender = mailSender;
        this.redisTemplate = redisTemplate;
    }

    // 인증번호 발송 - 회원가입
    public void sendMessageForSignUp(String email) throws Exception {
        MimeMessage message = createMessageForSignUp(email);

        log.info("보내는 대상 : " + email);
        log.info("인증 번호 : " + verificationCode);

        try {
            // 1. 메일 전송
            mailSender.send(message);
            // 2. 레디스 서버에 인증번호 저장
            redisTemplate.opsForValue().set(KEY_PREFIX + email, verificationCode, Duration.ofSeconds(LIMIT_TIME));
        } catch (MailException e){
            e.printStackTrace();
            throw new IllegalArgumentException();
        }
    }

    // 인증번호 발송 - 로그인
    public boolean sendMessageForLogin(String email) throws Exception {
        MimeMessage message = createMessageForLogin(email);

        log.info("보내는 대상 : " + email);
        log.info("인증 번호 : " + verificationCode);

        try {
            // 1. 메일 전송
            mailSender.send(message);
            // 2. 레디스 서버에 인증번호 저장
            redisTemplate.opsForValue().set(KEY_PREFIX + email, verificationCode, Duration.ofSeconds(LIMIT_TIME));
        } catch (MailException e){
            e.printStackTrace();
            throw new IllegalArgumentException();
        }

        return true;
    }

    // MimeMessage - 회원가입
    private MimeMessage createMessageForSignUp(String email) throws Exception {

        verificationCode = createVerificationCode();

        MimeMessage message = mailSender.createMimeMessage();

        message.addRecipients(Message.RecipientType.TO, email); // 보내는 대상
        message.setSubject("[cuki] 회원가입을 위한 인증번호를 확인해주세요.");  // 제목

        String html = "";
        html += "<div align='center' style='margin:50px;'>";
        html += "<h1>가입 안내</h1>";
        html += "<br>";
        html += "<p>아래 인증 코드를 입력하시면 회원가입이 완료됩니다. 감사합니다.</p>";
        html += "<br>";
        html += "<div align='center' style='border:1px solid black; font-family:verdana';>";
        html += "<h3 style='color:darkgreen;'>회원가입 코드</h3>";
        html += "<div style='font-size:130%'>";
        html += "CODE : <strong>";
        html += verificationCode;
        html += "</strong><div><br/> ";
        html += "<a href='https://cuki.io' target='_blank'>cuki 홈페이지 바로가기</a>";
        html += "</div>";

        message.setText(html, "utf-8", "html");  // 내용
        message.setFrom(new InternetAddress("verycona@gmail.com", "cuki")); // 보내는 사람

        return message;
    }

    // MimeMessage - 로그인
    private MimeMessage createMessageForLogin(String email) throws Exception {

        verificationCode = createVerificationCode();

        MimeMessage message = mailSender.createMimeMessage();

        message.addRecipients(Message.RecipientType.TO, email); // 보내는 대상
        message.setSubject("[cuki] 로그인을 위한 인증번호를 확인해주세요.");  // 제목

        String html = "";
        html += "<div align='center' style='margin:50px;'>";
        html += "<h1>로그인 안내</h1>";
        html += "<br>";
        html += "<p>아래 인증 코드를 입력하시면 로그인이 완료됩니다. 감사합니다.</p>";
        html += "<br>";
        html += "<div align='center' style='border:1px solid black; font-family:verdana';>";
        html += "<h3 style='color:darkgreen;'>로그인 코드</h3>";
        html += "<div style='font-size:130%'>";
        html += "CODE : <strong>";
        html += verificationCode;
        html += "</strong><div><br/> ";
        html += "<a href='https://cuki.io' target='_blank'>cuki 홈페이지 바로가기</a>";
        html += "</div>";

        message.setText(html, "utf-8", "html");  // 내용
        message.setFrom(new InternetAddress("verycona@gmail.com", "cuki")); // 보내는 사람

        return message;
    }

    // 인증코드 만들기
    private String createVerificationCode() {

        StringBuilder stringBuilder = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < 8; i++) {   // 인증코드 8자리
            stringBuilder.append(random.nextInt(10));
        }
        return stringBuilder.toString();
    }

    // 인증코드 검사
    public void verifyCode(String email, String uncheckedCode) {

        String actualCode = (String) redisTemplate.opsForValue().get(KEY_PREFIX + email);

        log.info("uncheckedCode : " + uncheckedCode + " actualCode : " + actualCode);
        log.info("email : " + email);


        if (!Boolean.TRUE.equals(redisTemplate.opsForValue().getOperations().hasKey(KEY_PREFIX + email))) {
            throw new NoSuchElementException("인증번호 입력시간이 만료되었습니다.");
        }

        if (Objects.equals(actualCode, uncheckedCode)) {
            redisTemplate.delete(KEY_PREFIX + email);
        } else {
            throw new IllegalArgumentException("인증번호가 틀렸습니다.");
        }
    }
}
