package com.testTask.cryptocurrency.controller;

import com.testTask.cryptocurrency.exception.InvalidNameException;
import com.testTask.cryptocurrency.exception.LargeSizeException;
import com.testTask.cryptocurrency.model.Price;
import com.testTask.cryptocurrency.model.WrapperPrice;
import com.testTask.cryptocurrency.service.PriceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/cryptocurrencies")
public class CryptocurrenciesController {

    @Autowired
    private PriceService priceService;

    @GetMapping("/minprice")
    public String minPriceSelectedCurrency(@RequestParam String name)    {
        try {
            double minPrice = priceService.getMinPriceSelectedCurrency(name);
            return  "$" + minPrice;
        }   catch (InvalidNameException ex) {
            return "Error: invalid parameter name";
        }   catch (Exception ex)    {
            return "Error";
        }
    }

    @GetMapping("/maxprice")
    public String maxPriceSelectedCurrency(@RequestParam String name)   {
        try {
            double maxPrice = priceService.getMaxPriceSelectedCurrency(name);
            return  "$" + maxPrice;
        }   catch (InvalidNameException ex) {
            return "Error: invalid parameter name";
        }   catch (Exception ex)    {
            return "Error";
        }
    }

    @GetMapping("")
    public String[] getSelectedPageWithSelectedElementSize(@RequestParam String name,
                      @RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
                      @RequestParam(value = "size", required = false, defaultValue = "10") Integer size)    {
        String[] exception = new String[1];
        try {
            if(size > priceService.getSizeDB()) {
                throw new LargeSizeException();
            }
            Pageable sorting = priceService.createPageable(page, size, name);
            Page<Price> elements = priceService.findElementsOfCurrency(sorting);
            String[] sortedElements = priceService.getSortedElements(name, size, elements);
            return sortedElements;
        }   catch (InvalidNameException ex) {
            exception[0] = "Error: invalid parameter name";
        }   catch (LargeSizeException ex)   {
            exception[0] = "Error: more than exists in the database. Max size = " + priceService.getSizeDB();
        }   catch (Exception ex)    {
            exception[0] = "Error";
        }
        return exception;
    }

    @GetMapping("/csv")
    public String csvReport(HttpServletResponse response) {
        try {
            HttpServletResponse customerResponse = priceService.settingsOfResponse(response);
            List<WrapperPrice> aPricesOfAllCurrencies = priceService.getArrayOfMinAndMaxPriceOfAllCurrencies();
            String sResponse = priceService.createAndSendCsvFile(customerResponse, aPricesOfAllCurrencies);
            return sResponse;
        }   catch (InvalidNameException ex) {
            return "Error: invalid parameter name";
        }   catch (IOException ex)  {
            return "Error: csvWriter";
        }   catch (Exception ex)    {
            return "Error";
        }

    }
}
