package com.questizzle.controllers;

import com.questizzle.models.Portal;
import com.questizzle.security.utils.JwtTokenUtil;
import com.questizzle.services.PortalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 * Created by Danny on 3/12/2017.
 */
@RestController
@RequestMapping("/api/portal")
public class PortalController {

    @Autowired
    private PortalService portalService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @RequestMapping(method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public List<Portal> showAllPortals() {
        List<Portal> portals = portalService.getAllPortals();

        portals = embedLinks(portals);

        return portals;
    }

    private List<Portal> embedLinks(List<Portal> portals) {
        for(int i = 0; i < portals.size(); i++) {
            String id     = portals.get(i).getIdentity();
            String userId = portals.get(i).getCreatedBy().getIdentity();

            portals.get(i).add(linkTo(methodOn(PortalController.class).getPortal(id)).withSelfRel());
            portals.get(i).getCreatedBy().add(linkTo(methodOn(UserController.class).getUser(userId)).withSelfRel());
        }

        return portals;
    }

    public Portal getPortal(String id) {
        return getPortal(id, null);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Portal getPortal(@PathVariable("id") String id, HttpServletRequest request) {
        String token = request.getHeader(UserController.tokenHeader);
        String username = jwtTokenUtil.getUsernameFromToken(token);

        Portal portal = portalService.getPortal(id, username);

        String userId = portal.getCreatedBy().getIdentity();

        portal.add(linkTo(methodOn(PortalController.class).getPortal(id)).withSelfRel());
        portal.getCreatedBy().add(linkTo(methodOn(UserController.class).getUser(userId)).withSelfRel());

        return portal;
    }

    @RequestMapping(value = "/search", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public List<Portal> searchPortals(@RequestParam(value="query") String query) {
        List<Portal> portals = portalService.searchPortals(query);

        portals = embedLinks(portals);

        return portals;
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public Portal createPortal(@RequestBody Portal portal) {
        Portal savedPortal = portalService.createPortal(portal);

        String id     = savedPortal.getIdentity();
        String userId = savedPortal.getCreatedBy().getIdentity();

        savedPortal.add(linkTo(methodOn(PortalController.class).getPortal(id)).withSelfRel());
        savedPortal.getCreatedBy().add(linkTo(methodOn(UserController.class).getUser(userId)).withSelfRel());

        return savedPortal;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    public Portal updatePortal(@PathVariable("id") String id, @RequestBody Portal portal) {
        Portal savedPortal = portalService.updatePortal(id, portal);

        String userId = savedPortal.getCreatedBy().getIdentity();

        savedPortal.add(linkTo(methodOn(PortalController.class).getPortal(id)).withSelfRel());
        savedPortal.getCreatedBy().add(linkTo(methodOn(UserController.class).getUser(userId)).withSelfRel());
        savedPortal.getModifiedBy().add(linkTo(methodOn(UserController.class).getUser(userId)).withSelfRel());

        return savedPortal;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    public void deletePortal(@PathVariable("id") String id) {
        portalService.deletePortal(id);
    }
}
