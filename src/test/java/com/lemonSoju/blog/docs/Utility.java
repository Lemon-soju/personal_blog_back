package com.lemonSoju.blog.docs;

import com.lemonSoju.blog.domain.Like;
import com.lemonSoju.blog.domain.Member;
import com.lemonSoju.blog.domain.Post;
import com.lemonSoju.blog.repository.MemberDataRepository;
import com.lemonSoju.blog.repository.PostDataRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Date;

@Component
public class Utility {
    private static final String KEY = "ryszg5rrIOkU3sPAKhsPuoLIXcJ7RX6O5N/StkVmzls=";

    @Autowired
    private MemberDataRepository memberDataRepository;
    @Autowired
    private PostDataRepository postDataRepository;

    public String mockJwt(Member member) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + Duration.ofMinutes(30).toMillis()); // 만료기간 30분
        Key key = Keys.hmacShaKeyFor(Base64.getDecoder().decode((KEY)));

        return Jwts.builder()
                .setSubject(member.getUid())
                .setIssuedAt(now) // 발급시간(iat)
                .setExpiration(expiration) // 만료시간(exp)
                .signWith(key) // 사용자 uid
                .compact();
    }

    public Member mockSignup(String uid) {
        Member member = Member.builder()
                .uid(uid)
                .pwd("test123!")
                .name("james")
                .build();
        return memberDataRepository.save(member);
    }

    public Post mockCreatePost(Member member) {
        Post post = Post.builder()
                .title("test title")
                .content("test content")
                .writer(member)
                .createDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .imagePreview(extractImage("test content"))
                .build();
        return postDataRepository.save(post);
    }

    public String extractImage(String content) {
        Document doc = Jsoup.parse(content);
        Element img = doc.select("img").first();
        if (img != null) {
            return img.attr("src");
        }
        return null;
    }

    public Like mockCreateLike(Member member, Post post) {
        Like like = new Like();
        like.addMemberAndPost(post, member);
        return like;
    }
}
