package com.xeiam.xchange.okcoin.service.marketdata;

import com.xeiam.xchange.Exchange;
import com.xeiam.xchange.ExchangeFactory;
import com.xeiam.xchange.ExchangeSpecification;
import com.xeiam.xchange.currency.CurrencyPair;
import com.xeiam.xchange.dto.marketdata.Trades;
import com.xeiam.xchange.okcoin.FuturesContract;
import com.xeiam.xchange.okcoin.FuturesDate;
import com.xeiam.xchange.okcoin.OkCoinExchange;
import com.xeiam.xchange.okcoin.SinceArg;
import com.xeiam.xchange.service.polling.marketdata.PollingMarketDataService;

import java.io.IOException;
import java.util.Date;

public class TradesIntegration {

    public static void main(String[] args) throws IOException {

        ExchangeSpecification exSpec = new ExchangeSpecification(OkCoinExchange.class);
        exSpec.setSecretKey("x");
        exSpec.setApiKey("x");

        // flag to set Use_Intl (USD) or China (default)
        exSpec.setExchangeSpecificParametersItem("Use_Intl", true);
        exSpec.setExchangeSpecificParametersItem("Use_Futures", true);
        exSpec.setExchangeSpecificParametersItem("Futures_Contract", FuturesContract.ThisWeek);
        Exchange okcoinExchange = ExchangeFactory.INSTANCE.createExchange(exSpec);

        trades(okcoinExchange);
    }

    private static void trades(Exchange exchange) throws IOException {
        final PollingMarketDataService marketDataService = exchange.getPollingMarketDataService();
        final FuturesDate futuresDate = new FuturesDate(new Date(Date.UTC(2014 - 1900, 10 - 1, 31, 8, 0, 0)));
        final SinceArg sinceArg = new SinceArg(0);
        final CurrencyPair pair = CurrencyPair.BTC_USD;

        final Trades trades = marketDataService.getTrades(pair, futuresDate, sinceArg);
        System.out.println(trades.toString());
    }
}
