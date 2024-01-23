package org.gogame.server.domain.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "user_friendship")
public class UserFriendshipEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "friendship_id_seq")
    @SequenceGenerator(name = "friendship_id_seq", allocationSize = 1)
    @Column(name = "friendship_id", nullable = false, unique = true)
    private Long friendshipId;

    @ManyToOne
    @JoinColumn(name = "user_a_id", referencedColumnName = "user_id")
    private UserEntity userAId;

    @ManyToOne
    @JoinColumn(name = "user_b_id", referencedColumnName = "user_id")
    private UserEntity userBId;
}