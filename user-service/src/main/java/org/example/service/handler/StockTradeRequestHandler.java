package org.example.service.handler;

import jakarta.transaction.Transactional;
import org.example.common.Ticker;
import org.example.exceptions.InsufficientBalanceException;
import org.example.exceptions.InsufficientSharesException;
import org.example.exceptions.UnknownTickerException;
import org.example.exceptions.UnknownUserException;
import org.example.repository.PortfolioItemRepository;
import org.example.repository.UserRepository;
import org.example.util.EntityMessageMapper;
import org.springframework.stereotype.Service;

@Service
public class StockTradeRequestHandler {

    private final UserRepository userRepository;
    private final PortfolioItemRepository portfolioItemRepository;

    public StockTradeRequestHandler(UserRepository userRepository, PortfolioItemRepository portfolioItemRepository) {
        this.userRepository = userRepository;
        this.portfolioItemRepository = portfolioItemRepository;
    }

    public org.example.user.UserInformation getUserInformation(org.example.user.UserInformationRequest request){
        var user = userRepository.findById(request.getUserId())
                .orElseThrow(()-> new UnknownUserException(request.getUserId()));
        var portfolioItems = portfolioItemRepository.findAllByUserId(request.getUserId());

        return EntityMessageMapper.toUserInformation(user, portfolioItems);
    }

    @Transactional
    public org.example.user.StockTradeResponse buyStock(org.example.user.StockTradeRequest request){

        this.validateTicker(request.getTicker());

        var user = userRepository.findById(request.getUserId())
                .orElseThrow(()-> new UnknownUserException(request.getUserId()));

        var totalPrice = request.getQuantity() * request.getPrice();
        validateUserBalance(request.getUserId(), user.getBalance(), totalPrice);

        user.setBalance(user.getBalance() - totalPrice);
        this.portfolioItemRepository.findByUserIdAndTicker(user.getId(), request.getTicker())
                .ifPresentOrElse(
                        item -> item.setQuantity(item.getQuantity() + request.getQuantity()),
                        () -> portfolioItemRepository.save(EntityMessageMapper.toPortfolioItem(request))
                );
        return EntityMessageMapper.toStockTradeResponse(request, user.getBalance());

    }


    @Transactional
    public org.example.user.StockTradeResponse sellStock(org.example.user.StockTradeRequest request){

        this.validateTicker(request.getTicker());

        var user = userRepository.findById(request.getUserId())
                .orElseThrow(()-> new UnknownUserException(request.getUserId()));

        var portfolioItem = this.portfolioItemRepository.findByUserIdAndTicker(user.getId(), request.getTicker())
                .filter(pi -> pi.getQuantity() >= request.getQuantity())
                .orElseThrow( () -> new InsufficientSharesException(user.getId()));

        var totalPrice = request.getQuantity() * request.getPrice();
        user.setBalance(user.getBalance() + totalPrice);
        portfolioItem.setQuantity(portfolioItem.getQuantity() - request.getQuantity());
        return EntityMessageMapper.toStockTradeResponse(request, user.getBalance());

    }



    private void validateTicker(org.example.common.Ticker ticker){
        if(Ticker.UNKNOWN.equals(ticker)){
            throw new UnknownTickerException();
        }
    }

    private void validateUserBalance(Integer userId, Integer userBalance, Integer totalPrice){

        if(totalPrice > userBalance)
            throw new InsufficientBalanceException(userId);
    }

}
