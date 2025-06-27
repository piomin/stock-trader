

Generate your API token. For now, the app supports [TwelveData](https://api.twelvedata.com) or [Profit](https://api.profit.com) APIs. Then, set your token as an environment variable:
```shell
export STOCK_API_KEY=<your_token>
```

Then, set your API provider:
```shell
export STOCK_API_PROVIDER=<twelvedata|profit>
```

Then, run a Postgres database.


Then, run the app:
```shell
mvn spring-boot:run
```