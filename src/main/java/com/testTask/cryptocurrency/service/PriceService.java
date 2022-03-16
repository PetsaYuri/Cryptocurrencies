package com.testTask.cryptocurrency.service;

import com.testTask.cryptocurrency.exception.InvalidNameException;
import com.testTask.cryptocurrency.model.Price;
import com.testTask.cryptocurrency.model.WrapperPrice;
import com.testTask.cryptocurrency.repository.PriceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class PriceService {

    @Autowired
    private PriceRepository priceRepository;

    public double getMinPriceSelectedCurrency(String currencyName) throws InvalidNameException {
        List<Price> prices = priceRepository.findAll();
        Double[] aSelectedCurrency = new Double[prices.size()];
        int i = 0;
        switch (currencyName)   {
            case "btc":
                for (Price price : prices)  {
                    aSelectedCurrency[i] = price.getBtc();
                    i++;
                }
                break;
            case "eth":
                for (Price price : prices)  {
                    aSelectedCurrency[i] = price.getEth();
                    i++;
                }
                break;
            case "xrp":
                for (Price price : prices)  {
                    aSelectedCurrency[i] = price.getXrp();
                    i++;
                }
                break;
            default:
                throw new InvalidNameException();
        }
        double minPrice = findMinPrice(aSelectedCurrency);
        return minPrice;
    }

    public double findMinPrice(Double[] aSelectedCurrency)    {
        boolean isSorted = false;
        double buf;
        while (!isSorted)   {
            isSorted = true;
            for(int i = 0; i<aSelectedCurrency.length-1; i++) {
                if(aSelectedCurrency[i] > aSelectedCurrency[i+1])   {
                    isSorted = false;
                    buf = aSelectedCurrency[i];
                    aSelectedCurrency[i] = aSelectedCurrency[i+1];
                    aSelectedCurrency[i+1] = buf;
                }
            }
        }
        return aSelectedCurrency[0];
    }

    public double getMaxPriceSelectedCurrency(String currencyName) throws InvalidNameException  {
        List<Price> prices = priceRepository.findAll();
        Double[] aSelectedCurrency = new Double[prices.size()];
        int i = 0;
        switch (currencyName)   {
            case "btc":
                for (Price price : prices)  {
                    aSelectedCurrency[i] = price.getBtc();
                    i++;
                }
                break;
            case "eth":
                for (Price price : prices)  {
                    aSelectedCurrency[i] = price.getEth();
                    i++;
                }
                break;
            case "xrp":
                for (Price price : prices)  {
                    aSelectedCurrency[i] = price.getXrp();
                    i++;
                }
                break;
            default:
                throw new InvalidNameException();
        }
        double maxPrice = findMaxPrice(aSelectedCurrency);
        return maxPrice;
    }

    public double findMaxPrice(Double[] aSelectedCurrency)    {
        boolean isSorted = false;
        double buf;
        while (!isSorted)   {
            isSorted = true;
            for(int i = 0; i<aSelectedCurrency.length-1; i++) {
                if(aSelectedCurrency[i] < aSelectedCurrency[i+1])   {
                    isSorted = false;
                    buf = aSelectedCurrency[i];
                    aSelectedCurrency[i] = aSelectedCurrency[i+1];
                    aSelectedCurrency[i+1] = buf;
                }
            }
        }
        return aSelectedCurrency[0];
    }

    public Pageable createPageable(Integer page, Integer size, String sort){
        Pageable pageable = new Pageable() {
            @Override
            public int getPageNumber() {
                return page;
            }

            @Override
            public int getPageSize() {
                return size;
            }

            @Override
            public long getOffset() {
                return 0;
            }

            @Override
            public Sort getSort() {
                return Sort.by(sort);
            }

            @Override
            public Pageable next() {
                return null;
            }

            @Override
            public Pageable previousOrFirst() {
                return null;
            }

            @Override
            public Pageable first() {
                return null;
            }

            @Override
            public Pageable withPage(int pageNumber) {
                return null;
            }

            @Override
            public boolean hasPrevious() {
                return false;
            }
        };
        return pageable;
    }

    public Page<Price> findElementsOfCurrency(Pageable pageable)    {
        return priceRepository.findAll(pageable);
    }

    public String[] getSortedElements(String currencyName, int size, Page<Price> elements) throws InvalidNameException {
        int i = 0;
        String[] sortedElements = new String[size];
        switch (currencyName)   {
            case "btc":
                for(Price price : elements)    {
                    sortedElements[i] = "$" + price.getBtc();
                    i++;
                }
                break;
            case "eth":
                for(Price price : elements)    {
                    sortedElements[i] = "$" + price.getEth();
                    i++;
                }
                break;
            case "xrp":
                for(Price price : elements)    {
                    sortedElements[i] = "$" + price.getXrp();
                    i++;
                }
                break;
            default:
                throw new InvalidNameException();
        }
        return sortedElements;
    }

    public int getSizeDB()  {
        List<Price> elements = priceRepository.findAll();
        int size = elements.size();
        return size;
    }

    public WrapperPrice getMinAndMaxPriceOfSelectedCurrency(String currencyName) throws InvalidNameException   {
        double minPrice = getMinPriceSelectedCurrency(currencyName);
        double maxPrice = getMaxPriceSelectedCurrency(currencyName);
        WrapperPrice wrapperPrice = new WrapperPrice(currencyName, minPrice, maxPrice);
        return wrapperPrice;
    }

    public List<WrapperPrice> getArrayOfMinAndMaxPriceOfAllCurrencies() throws InvalidNameException {
        WrapperPrice btc = getMinAndMaxPriceOfSelectedCurrency("btc");
        WrapperPrice eth = getMinAndMaxPriceOfSelectedCurrency("eth");
        WrapperPrice xrp = getMinAndMaxPriceOfSelectedCurrency("xrp");

        List<WrapperPrice> aPricesOfAllCurrencies = new ArrayList<>();
        aPricesOfAllCurrencies.add(btc);
        aPricesOfAllCurrencies.add(eth);
        aPricesOfAllCurrencies.add(xrp);

        return aPricesOfAllCurrencies;
    }

    public HttpServletResponse settingsOfResponse(HttpServletResponse response)    {
        response.setContentType("text/csv");
        String filename = "cryptocurrencies.csv";

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=" + filename;

        response.setHeader(headerKey, headerValue);
        return response;
    }

    public String createAndSendCsvFile(HttpServletResponse response, List<WrapperPrice> aPricesOfAllCurrencies)
            throws IOException, InvalidNameException {
        ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(), CsvPreference.EXCEL_NORTH_EUROPE_PREFERENCE);
        String[] csvHeader = {"Currency", "Min Price", "Max Price"};
        String[] currencyMapping = {"currency", "minPrice", "maxPrice"};
        csvWriter.writeHeader(csvHeader);

        for(WrapperPrice currency : aPricesOfAllCurrencies)    {
            csvWriter.write(currency, currencyMapping);
        }

        csvWriter.close();
        return "csv file successfully created and sending";
    }
}
