package io.hpp.redistest.repository;

import io.hpp.redistest.RedisConfig;
import io.hpp.redistest.RedisRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@DisplayName("REDIS Repository Test")
@Import({RedisConfig.class})
@SpringBootTest
public class RedisRepositoryTest {

    private final RedisRepository redisRepository;

    public RedisRepositoryTest(@Autowired RedisRepository redisRepository) {
        this.redisRepository = redisRepository;
    }

    @BeforeEach
    public void init(){
        redisRepository.delAllToken();
    }

    @Test
    public void givenToken_whenInsert_thenSuccess(){
        String newToken = genToken();
        redisRepository.addToken(newToken);
        Long count = redisRepository.getCount();
        assertThat(count).isEqualTo(1);
    }

    @Test
    public void givenToken_whenGetToken_thenToken(){
        String newToken = genToken();
        redisRepository.addToken(newToken);

        String foundToken = redisRepository.getToken(newToken);

        assertThat(foundToken).isEqualTo(newToken);
    }

    @Test
    public void givenToken_whenManyGetToken_thenToken(){
        String newToken = "";
        for(int i = 0 ; i < 10; i++){
            newToken = genToken();
            redisRepository.addToken(newToken);
        }

        Long count = redisRepository.getCount();

        log.debug(">>>>>>>>>>>>>>>>>>>> redis count [{}] <<<<<<<<<<<<<<<<<<<<<<",count);
        redisRepository.displayAllMembers();
        assertThat(count).isEqualTo(10);
    }

    public String genToken(){
        String newToken = String.format("%s%s", System.currentTimeMillis(),UUID.randomUUID().toString());
        log.debug("new Token [{}]",newToken);
        return newToken;
    }

    @AfterEach
    public void after(){
        redisRepository.delAllToken();
    }


}
