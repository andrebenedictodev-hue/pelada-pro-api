package com.ab.peladapro.peladaproapi.domain.service;

import com.ab.peladapro.peladaproapi.domain.model.Friendship;
import com.ab.peladapro.peladaproapi.domain.model.Usuario;
import com.ab.peladapro.peladaproapi.domain.dao.FriendshipDAO;
import com.ab.peladapro.peladaproapi.domain.dao.UsuarioDAO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FriendshipService {

    private final FriendshipDAO friendshipDAO;
    private final UsuarioDAO usuarioDAO;

    public FriendshipService(FriendshipDAO friendshipDAO,
            UsuarioDAO usuarioDAO) {
        this.friendshipDAO = friendshipDAO;
        this.usuarioDAO = usuarioDAO;
    }

    public List<Usuario> listFriends(Usuario usuario) {
        return friendshipDAO.findByUserId(usuario.getUuid()).stream()
                .map(friendship -> usuarioDAO.findFirstByUuid(friendship.getFriendUserId()).orElse(null))
                .filter(item -> item != null)
                .toList();
    }

    public void createFriendshipPair(String a, String b) {
        if (!friendshipDAO.existsByUserIdAndFriendUserId(a, b)) {
            Friendship friendship = new Friendship();
            friendship.setUserId(a);
            friendship.setFriendUserId(b);
            friendshipDAO.save(friendship);
        }
        if (!friendshipDAO.existsByUserIdAndFriendUserId(b, a)) {
            Friendship friendship = new Friendship();
            friendship.setUserId(b);
            friendship.setFriendUserId(a);
            friendshipDAO.save(friendship);
        }
    }
}
