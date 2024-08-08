package org.example.service.handler;

import org.example.exceptions.UnknownUserException;
import org.example.repository.PortfolioItemRepository;
import org.example.repository.UserRepository;
import org.example.util.EntityMessageMapper;

public class UserInformationRequestHandler {

    private final UserRepository userRepository;
    private final PortfolioItemRepository portfolioItemRepository;

    public UserInformationRequestHandler(UserRepository userRepository, PortfolioItemRepository portfolioItemRepository) {
        this.userRepository = userRepository;
        this.portfolioItemRepository = portfolioItemRepository;
    }

    public org.example.user.UserInformation getUserInformation(org.example.user.UserInformationRequest request){
        var user = userRepository.findById(request.getUserId())
                .orElseThrow(()-> new UnknownUserException(request.getUserId()));
        var portfolioItems = portfolioItemRepository.findAllByUserId(request.getUserId());

        return EntityMessageMapper.toUserInformation(user, portfolioItems);
    }
;
}
