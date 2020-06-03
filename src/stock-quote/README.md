The stock-quote microservice gets the price of a specified stock.

### Build stock-quote service
Here are the steps to recompile and rebuild stock-quote image:
```
# cd stocktrader-jil/src/stock-quote/
# mvn package
# docker build -t stock-quote:latest -t stocktradersjilv2/stock-quote:latest .
# docker tag stock-quote:latest stocktradersjilv2/stock-quote:latest
# docker push stocktradersjilv2/stock-quote:latest
```