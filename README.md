# Stock Trader Application

## Motivation

This application aims to provide a simple way to analyze trends in trade stocks. It loads data from an external API, inserts it to the local database and provides a simple interface to analyze the data. The application is based on the [TA4J](https://ta4j.github.io/) library.

## Usage

Generate your API token. For now, the app supports [TwelveData](https://api.twelvedata.com) or [Profit](https://api.profit.com) APIs. Then, set your token as an environment variable:
```shell
export STOCK_API_KEY=<your_token>
```

Then, set your API provider:
```shell
export STOCK_API_PROVIDER=<twelvedata|profit>
```

Then, run a Postgres database. You can use the following command for Docker:
```shell
docker run --name stock-trader-db -p 5432:5432 -e POSTGRES_PASSWORD=example -d postgres
```
Alternatively, you can use the following command for Docker Compose:
```shell
docker compose up -d
```

Then, export your database connection properties:
```shell
export DATABASE_USER=<your_user>
export DATABASE_PASS=<your_pass>
```

Finally, run the app:
```shell
mvn spring-boot:run
```