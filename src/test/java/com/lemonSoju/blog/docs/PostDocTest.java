package com.lemonSoju.blog.docs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lemonSoju.blog.domain.User;
import com.lemonSoju.blog.dto.request.DeletePostRequestDto;
import com.lemonSoju.blog.dto.request.PostWriteRequestDto;
import com.lemonSoju.blog.dto.request.UserLoginRequestDto;
import com.lemonSoju.blog.repository.UserDataRepository;
import com.lemonSoju.blog.service.PostService;
import com.lemonSoju.blog.service.UserService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.security.Key;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs(uriScheme = "http", uriHost = "http://3.35.179.185", uriPort = 80)
@ExtendWith(RestDocumentationExtension.class)
@Transactional
public class PostDocTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private PostService postService;
    @Autowired
    private UserService userService;
    @Autowired
    private UserDataRepository userDataRepository;
    @Autowired
    private ObjectMapper objectMapper;
    private static final String KEY = "ryszg5rrIOkU3sPAKhsPuoLIXcJ7RX6O5N/StkVmzls=";

    @Test
    @DisplayName("?????????")
    void postWrite() throws Exception {

        // given
        String jwt = createToken();

        PostWriteRequestDto request = PostWriteRequestDto.builder()
                .title("test01")
                .content("test01-content")
                .build();

        String json = objectMapper.writeValueAsString(request);

        // expected
        mockMvc.perform(post("/post/write")
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON)
                        .header("accessToken", jwt)
                        .content(json))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("post-write",
                        requestFields(
                                fieldWithPath("title").description("??????"),
                                fieldWithPath("content").description("??????")
                        ),
                        responseFields(
                                fieldWithPath("postId").description("??? ??????")
                        )
                ));
    }

    @Test
    @DisplayName("??? ?????? ????????????")
    void getPosts() throws Exception {
        // expected
        mockMvc.perform(get("/post")
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON)
                ).andDo(print())
                .andExpect(status().isOk())
                .andDo(document("get-posts",
                        responseFields(
                                fieldWithPath("[].postId").description("??? ??????"),
                                fieldWithPath("[].title").description("??? ??????"),
                                fieldWithPath("[].content").description("??? ??????"),
                                fieldWithPath("[].writer").description("?????????"),
                                fieldWithPath("[].createDate").description("????????????")
                        )
                ));
    }


    @Test
    @DisplayName("??? ??????")
    void postDelete() throws Exception {

        // given
        String jwt = createToken();
        List<Long> deleteList = List.of(6L, 8L);

        DeletePostRequestDto request = DeletePostRequestDto.builder()
                .checkedInputs(deleteList)
                .build();

        String json = objectMapper.writeValueAsString(request);

        // expected
        mockMvc.perform(post("/user/post/delete")
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON)
                        .header("accessToken", jwt)
                        .content(json))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("post-delete",
                        requestFields(
                                fieldWithPath("checkedInputs").description("?????? ?????????")
                        )
                ));
    }

    private String createToken() {
        User findUser = userDataRepository.findByUid("user01").get();

        Key key = Keys.hmacShaKeyFor(Base64.getDecoder().decode((KEY)));

        // jwt ??????
        Date now = new Date();
        Date expiration = new Date(now.getTime() + Duration.ofDays(1).toMillis()); // ???????????? 1???

        String jws = Jwts.builder()
                .setSubject(findUser.getUid())
                .setIssuedAt(now) // ????????????(iat)
                .setExpiration(expiration) // ????????????(exp)
                .signWith(key) // ????????? uid
                .compact();
        return jws;
    }
}