package com.example.domain.controller;

import com.example.domain.entity.Member;
import com.example.domain.entity.Role;
import com.example.domain.response.MemberRes;
import com.example.domain.service.MemberService;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@Tag(name = "social_login", description = "social_login API 입니다.")
@RequestMapping("/members")
public class socialLoginController {

    private final MemberService memberService;


    @Operation(summary = "카카오 로그인", description = "카카오 로그인 메소드입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "401", description = "인증 실패"),
            @ApiResponse(responseCode = "404", description = "사용자 없음"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @GetMapping("/kakaologin")
    public ResponseEntity<?> kakaoRequestAccessToken(@RequestParam String code) {
        //카카오 서버에 POST 방식으로 엑세스 토큰을 요청
        //RestTemplate를 이용
        try {
            System.out.println(code);
            String accessToken = memberService.getKaKaoAccessToken(code);

            ResponseEntity<String> memberInfoResponse = memberService.getKakaoMember(accessToken);

            JsonParser jp = new JsonParser();
//            JsonObject jo = jp.parse(memberInfoResponse.getBody()).getAsJsonObject();

            JsonObject memberJsonObject = jp.parse(memberInfoResponse.getBody()).getAsJsonObject();
            JsonObject memberAccountObject = jp.parse(memberJsonObject.get("kakao_account").toString()).getAsJsonObject();
            String nickname = jp.parse(memberAccountObject.get("profile").toString()).getAsJsonObject().get("nickname").getAsString();
            String email = memberAccountObject.get("email").getAsString();

            boolean isMember = memberService.findUserBySocialId(email);

            if (!isMember) {
                Member member = new Member(email, nickname, "");
                MemberRes memberRes = new MemberRes(member.getNickname(), member.getProfile());

                memberService.joinMember(member);

                return ResponseEntity.ok(memberRes);
            }
            //else
            return ResponseEntity.ok("success");
        } catch (Exception e) {
            return ResponseEntity.status(500).body(e);
        }

    }

    @Operation(summary = "구글 로그인", description = "구글 로그인 메소드입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "401", description = "인증 실패"),
            @ApiResponse(responseCode = "404", description = "사용자 없음"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @GetMapping("/googlelogin")
    public ResponseEntity<?> GoogleRequestAccessToken(@RequestParam String code) {
        try{
            String accessToken = memberService.getGoogleAccessToken(code);

            ResponseEntity<String> memberInfoResponse = memberService.getGoogleMember(accessToken);

            JsonParser jp = new JsonParser();

            JsonObject memberJsonObject = jp.parse(memberInfoResponse.getBody()).getAsJsonObject();

            String email = memberJsonObject.get("email").getAsString();
            boolean isMember = memberService.findUserBySocialId(email);

            if (!isMember) {
                Member member = new Member(email, "", "");
                MemberRes memberRes = new MemberRes(member.getNickname(), member.getProfile());

                memberService.joinMember(member);

                return ResponseEntity.ok(memberRes);
            }
            //else
            return ResponseEntity.ok("success");

        } catch(Exception e) {
            return ResponseEntity.status(500).body(e);
        }
    }
}
