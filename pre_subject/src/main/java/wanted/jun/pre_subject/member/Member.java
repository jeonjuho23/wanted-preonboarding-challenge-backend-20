package wanted.jun.pre_subject.member;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "members")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    Long id;
    @Column(name = "id", length = 100)
    String userId;
    @Column(name = "password", length = 100)
    String userPassword;
    @Column(name = "name", length = 20)
    String userName;

    @Builder(access = AccessLevel.PRIVATE)
    private Member(String userId, String userPassword, String userName) {
        this.userId = userId;
        this.userPassword = userPassword;
        this.userName = userName;
    }

    public static Member signUp(String userId, String userPassword, String userName) {
        return Member.builder()
                .userId(userId)
                .userPassword(userPassword)
                .userName(userName)
                .build();
    }

}
