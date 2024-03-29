package org.gogame.server.repositories;

import org.gogame.server.domain.entities.LeaderboardEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class LeaderboardRepositoryIntegrationTests {

    private final UserRepository userRepo;

    private final LeaderboardRepository leaderboardRepo;

    @Autowired
    public LeaderboardRepositoryIntegrationTests(UserRepository userRepo,
                                                 LeaderboardRepository leaderboardRepo) {
        this.userRepo = userRepo;
        this.leaderboardRepo = leaderboardRepo;
    }

    @Test
    public void testThatLeaderboardCanBeCreatedAndRecalled() {
        LeaderboardEntity leaderboardEntityA = TestData.LeaderboardUtils.createA(userRepo);
        leaderboardRepo.save(leaderboardEntityA);
        Optional<LeaderboardEntity> result = leaderboardRepo.findById(leaderboardEntityA.getUser().getUserId());
        assertThat(result).isPresent();
        assertThat(result).contains(leaderboardEntityA);
    }

    @Test
    public void testThatMultipleLeaderboardsCanBeCreatedAndRecalled() {
        LeaderboardEntity leaderboardEntityA = TestData.LeaderboardUtils.createA(userRepo);
        leaderboardRepo.save(leaderboardEntityA);
        LeaderboardEntity leaderboardEntityB = TestData.LeaderboardUtils.createB(userRepo);
        leaderboardRepo.save(leaderboardEntityB);
        LeaderboardEntity leaderboardEntityC = TestData.LeaderboardUtils.createC(userRepo);
        leaderboardRepo.save(leaderboardEntityC);

        Iterable<LeaderboardEntity> result = leaderboardRepo.findAll();
        Iterable<LeaderboardEntity> expected = new ArrayList<>(Arrays.asList(leaderboardEntityA, leaderboardEntityB, leaderboardEntityC));
        assertThat(result).containsExactlyInAnyOrderElementsOf(expected);
    }

    @Test
    public void testThatLeaderboardCanBeUpdated() {
        LeaderboardEntity leaderboardEntityA = TestData.LeaderboardUtils.createA(userRepo);
        leaderboardRepo.save(leaderboardEntityA);
        leaderboardEntityA.setScore(16L);
        leaderboardRepo.save(leaderboardEntityA);
        Optional<LeaderboardEntity> result = leaderboardRepo.findById(leaderboardEntityA.getUser().getUserId());
        assertThat(result).isPresent();
        assertThat(result).contains(leaderboardEntityA);
    }

    @Test
    public void testThatLeaderboardCanBeDeleted() {
        LeaderboardEntity leaderboardEntityA = TestData.LeaderboardUtils.createA(userRepo);
        leaderboardRepo.save(leaderboardEntityA);
        leaderboardRepo.deleteById(leaderboardEntityA.getUser().getUserId());
        Optional<LeaderboardEntity> result = leaderboardRepo.findById(leaderboardEntityA.getUser().getUserId());
        assertThat(result).isEmpty();
    }
}