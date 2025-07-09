package pl.piomin.services.stocktrader.strategy;

import org.springframework.stereotype.Service;
import org.ta4j.core.*;
import org.ta4j.core.backtest.BarSeriesManager;
import org.ta4j.core.indicators.adx.ADXIndicator;
import org.ta4j.core.indicators.adx.MinusDIIndicator;
import org.ta4j.core.indicators.adx.PlusDIIndicator;
import org.ta4j.core.indicators.helpers.ClosePriceIndicator;
import org.ta4j.core.rules.CrossedDownIndicatorRule;
import org.ta4j.core.rules.CrossedUpIndicatorRule;
import org.ta4j.core.rules.OverIndicatorRule;

@Service
public class ADXCrossedUpDIStrategy implements BuildAndRunStrategy {

    @Override
    public TradingRecord buildAndRun(BarSeries series) {
        ClosePriceIndicator closePrice = new ClosePriceIndicator(series);

        final int adxBarCount = 14;
        final ADXIndicator adxIndicator = new ADXIndicator(series, adxBarCount);
        final OverIndicatorRule adxOver20Rule = new OverIndicatorRule(adxIndicator, 20);

        final PlusDIIndicator plusDIIndicator = new PlusDIIndicator(series, adxBarCount);
        final MinusDIIndicator minusDIIndicator = new MinusDIIndicator(series, adxBarCount);

        final Rule plusDICrossedUpMinusDI = new CrossedUpIndicatorRule(plusDIIndicator, minusDIIndicator);
        final Rule plusDICrossedDownMinusDI = new CrossedDownIndicatorRule(plusDIIndicator, minusDIIndicator);
        final Rule entryRule = adxOver20Rule.and(plusDICrossedUpMinusDI);
        final Rule exitRule = adxOver20Rule.and(plusDICrossedDownMinusDI);

        Strategy strategy = new BaseStrategy(entryRule, exitRule);
        BarSeriesManager seriesManager = new BarSeriesManager(series);
        TradingRecord tradingRecord = seriesManager.run(strategy);
        return tradingRecord;
    }
}
