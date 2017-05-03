package com.questizzle.services;

import com.questizzle.exceptions.AccessDeniedException;
import com.questizzle.models.MiniUser;
import com.questizzle.models.Portal;
import com.questizzle.exceptions.DataNotFoundException;
import com.questizzle.models.User;
import com.questizzle.repositories.PortalRepository;
import com.questizzle.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Created by Danny on 3/20/2017.
 */
@Service
public class PortalService {

    @Autowired
    private PortalRepository portalRepository;

    @Autowired
    private UserRepository userRepository;

    public PortalService() {}

    public List<Portal> getAllPortals() {
        return portalRepository.findAll();
    }

    public boolean isNotAllowed(String portalId, String username) {
        boolean isNotAllowed = true;

        Portal portal = portalRepository.findOne(portalId);

        if(portal.getCreatedBy().getUsername().equalsIgnoreCase(username)) {
            isNotAllowed = false;
        }
        else if(portal.getAdmins().contains(username)) {
            isNotAllowed = false;
        }
        else if(portal.getUsers().contains(username)) {
            isNotAllowed = false;
        }

        return isNotAllowed;
    }

    public Portal getPortal(String id, String username) throws RuntimeException {
        Portal portal = portalRepository.findOne(id);

        if(portal == null) {
            throw new DataNotFoundException(id);
        }

        if(isNotAllowed(id, username)) {
            throw new AccessDeniedException();
        }

        return portal;
    }

    public List<Portal> searchPortals(String query) {
        List<Portal> portals = portalRepository.findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(query, query);
        return portals;
    }

    public Portal createPortal(Portal portal) {
        MiniUser userInfo = portal.getCreatedBy();

        if(userInfo.getUsername() == null) {
            throw new UsernameNotFoundException(userInfo.getUsername());
        }

        if(userInfo.getIdentity() == null) {
            User user = userRepository.findByUsername(userInfo.getUsername());
            userInfo.setIdentity(user.getIdentity());
            portal.setCreatedBy(userInfo);
        }

        portal = trimListValues(portal);

        portal.setDateCreated(new Date());

        return portalRepository.save(portal);
    }

    private Portal trimListValues(Portal portal) {
        for(int i = 0; i < portal.getTopics().size(); i++) {
            String topic = portal.getTopics().get(i).trim();
            portal.getTopics().set(i, topic);
        }

        for(int i = 0; i < portal.getAdmins().size(); i++) {
            String admin = portal.getAdmins().get(i).trim();
            portal.getAdmins().set(i, admin);
        }

        for(int i = 0; i < portal.getUsers().size(); i++) {
            String user = portal.getUsers().get(i).trim();
            portal.getUsers().set(i, user);
        }

        return portal;
    }

    public Portal updatePortal(String id, Portal portal) throws DataNotFoundException {
        Portal existingPortal = portalRepository.findOne(id);

        if(existingPortal == null) {
            throw new DataNotFoundException(id);
        }

        MiniUser userInfo = portal.getModifiedBy();

        if(userInfo.getUsername() == null) {
            throw new UsernameNotFoundException(userInfo.getUsername());
        }

        if(userInfo.getIdentity() == null) {
            User user = userRepository.findByUsername(userInfo.getUsername());
            userInfo.setIdentity(user.getIdentity());
            existingPortal.setModifiedBy(userInfo);
        }

        portal = trimListValues(portal);

        existingPortal.setName(portal.getName());
        existingPortal.setDescription(portal.getDescription());
        existingPortal.setAdmins(portal.getAdmins());
        existingPortal.setUsers(portal.getUsers());
        existingPortal.setTopics(portal.getTopics());
        existingPortal.setDateModified(new Date());

        Portal savedPortal = portalRepository.save(existingPortal);

        return savedPortal;
    }

    public void deletePortal(String id) throws DataNotFoundException {
        if(portalRepository.exists(id)) {
            portalRepository.delete(id);
        } else {
            throw new DataNotFoundException(id);
        }
    }
}
