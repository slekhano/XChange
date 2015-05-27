package com.xeiam.xchange.okcoin.service.polling;

import java.io.IOException;
import java.util.concurrent.Future;

import com.xeiam.xchange.Exchange;
import com.xeiam.xchange.currency.CurrencyPair;
import com.xeiam.xchange.dto.marketdata.OrderBook;
import com.xeiam.xchange.dto.marketdata.Ticker;
import com.xeiam.xchange.dto.marketdata.Trades;
import com.xeiam.xchange.okcoin.FuturesContract;
import com.xeiam.xchange.okcoin.FuturesDate;
import com.xeiam.xchange.okcoin.OkCoinAdapters;
import com.xeiam.xchange.okcoin.SinceArg;
import com.xeiam.xchange.okcoin.dto.marketdata.OkCoinTrade;
import com.xeiam.xchange.service.polling.marketdata.PollingMarketDataService;

public class OkCoinFuturesMarketDataService extends OkCoinMarketDataServiceRaw implements PollingMarketDataService {
  /** Default contract to use */
  private final FuturesContract futuresContract;

  /**
   * Constructor
   *
   * @param exchange
   */
  public OkCoinFuturesMarketDataService(Exchange exchange, FuturesContract futuresContract) {

    super(exchange);
    
    this.futuresContract = futuresContract;
  }

  @Override
  public Ticker getTicker(CurrencyPair currencyPair, Object... args) throws IOException {
    if (args.length > 0) {
      return OkCoinAdapters.adaptTicker(getFuturesTicker(currencyPair, (FuturesContract) args[0]), currencyPair);
    } else {
      return OkCoinAdapters.adaptTicker(getFuturesTicker(currencyPair, futuresContract), currencyPair);
    }
  }

  @Override
  public OrderBook getOrderBook(CurrencyPair currencyPair, Object... args) throws IOException {
    if (args.length > 0) {
      return OkCoinAdapters.adaptOrderBook(getFuturesDepth(currencyPair, (FuturesContract) args[0]), currencyPair);
    } else {
      return OkCoinAdapters.adaptOrderBook(getFuturesDepth(currencyPair, futuresContract), currencyPair);
    }
  }

  @Override
  public Trades getTrades(CurrencyPair currencyPair, Object... args) throws IOException {
    FuturesContract contract = null;
    FuturesDate date = null;
    SinceArg since = null;

    for (Object a: args) {
      if (a instanceof FuturesContract) contract = (FuturesContract)a;
      if (a instanceof FuturesDate) date = (FuturesDate)a;
      if (a instanceof SinceArg) since = (SinceArg)a;
    }

    if (contract != null && date != null) {
      throw new RuntimeException("Only contract or date expected: both passed");
    }
    if (date != null)
      if (since == null) throw new RuntimeException("Since expected for date: since not passed");

    final OkCoinTrade[] trades = (null != contract)
            ? getFuturesTrades(currencyPair, contract)
            : getFuturesTradesHistory(currencyPair, date, since);
    return OkCoinAdapters.adaptTrades(trades, currencyPair);
  }
}
