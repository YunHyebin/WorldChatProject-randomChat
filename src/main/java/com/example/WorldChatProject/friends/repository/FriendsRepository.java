package com.example.WorldChatProject.friends.repository;

import com.example.WorldChatProject.friends.dto.FriendsStatement;
import com.example.WorldChatProject.friends.entity.Friends;
import com.example.WorldChatProject.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FriendsRepository extends JpaRepository<Friends, Long> {
//    List<Friends> findByUserId(long userId);

    Optional<Friends> findByUserAndFriends(User requester, User receiver);


    @Query(value = "select user" +
            "         from friends" +
            "        where friends = :friends ", nativeQuery = true)
    List<Long> findReceiverIdByUserId(@Param("friends") long userId);

    List<Friends> findByFriends(User user);

    List<Friends> findByFriendsAndStatement(User user, FriendsStatement friendsStatement);

    List<Friends> findByUserAndStatement(User user, FriendsStatement friendsStatement);

//    List<Friends> findByRequester(User requester);
//
//    Friends findRequestByRequesterAndReceiver(User receiver, User requester);
//
//
//    List<Friends> findByRequesterAndIsFromAndStatement(User requester, boolean isFrom, FriendsStatement statement);

}
