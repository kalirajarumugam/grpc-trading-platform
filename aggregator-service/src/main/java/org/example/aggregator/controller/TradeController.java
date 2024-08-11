package org.example.aggregator.controller;

import org.example.aggregator.service.TradeService;
import org.example.user.StockTradeRequest;
import org.example.user.StockTradeResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("trade")
public class TradeController {

    @Autowired
    private TradeService tradeService;

    public StockTradeResponse trade(@RequestBody StockTradeRequest request){
        return tradeService.trade(request);
    }

}
