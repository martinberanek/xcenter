package cz.martinberanek.xcenter.xcenter;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class XCenterAuthenticationService {

    private static final Logger log = LoggerFactory.getLogger(XCenterAuthenticationService.class);

    private static final String CACHE_NAME = "xcenter_cookies";

    private final XCenterConfiguration configuration;

    @Cacheable(cacheNames = CACHE_NAME)
    public String getCookies() {
//        return ".AspNetCore.Cookies=CfDJ8C2i0saZcaxKjsnjvLxjj-d0PXaJl0jgJUuRB7v19FROSNlLsHm_dwAxSW88RGfSgvFt4PPTC66utmWjhId5iw6pGo4gI3ubY3QEtxNahbNw7Boepf65zUqcWkSh_fi0cYrZ9E1iO6RlFbJuoPFaYzTbXKPVDq3SQcfnxk38ZrMBB76t-lNxULWdYA3MP5fMynkvLhB8B84mlgswNDNYyR45dpmQg_sJbXDFY2lx-mOpsuZCzjjXueaS_NnVoXPo03kzw0m9Z2rEpJSSvweZ1toI1DviyLX7J78BmHXocqdk7RoOx0Kpf0FzMJNtdcdA0w; .AspNetCore.Antiforgery.itcR9eZeppo=CfDJ8C70bYMZ8yJBs1ubAavQFHyFahPDCq22gz1ookFcyR7bgxInI3f9jQ5E-O8sjtIzizPD0AnVEsmxAlenb4obdSvRFZhYbYVqrCMbE_UBCjddHWSXGGd2urQfMiPCsy9ph3XscYALpbdYXZONRDU4fEI; .OpenIdAuth=CfDJ8C70bYMZ8yJBs1ubAavQFHyjODJu4hc9whnWcGbf30n8wuaU5-pprF62uYvBsdBlEZ6-6ikFdRUu31Vl04XVPVlgkX28ZVczwcZRvN8VLK4TnZAoUh_5K1V_pnkELYuKU0LSoG4umUmHxp5lYwsJm7tmeFKKMN6dZXh8tgaSiSKdIArfhdk4iBTa2cRt6UzeYVbnOBnZJSkZP2PJj5rUcYSUx2DKvx-LJvF4GvJujuFcOlOf8F8bzmNlIQbcwab-QPFM3l2KRKS7XDM11hURQ5-K8x6K2n1Fwqr-AArxOz48z1efrCcIk4wzXX6pFp93O425mb4B1dzoSWa9kATjgHPM7RlEobTqvmJExtfQTUx-5JYDK2KCm0JafPucmo4JGV7jNJ-q1ag0BjpsejmbKQOSrb_l8RPujTEZLd9HlQSkgGLUZ6TcvggcWpSHQSE9R5uPJ0TzjclQIY7x7iwkPuTYQHlGUEheCCwdKaMll-5_f8K2E-4TByby2q9Iwee0gg; .AspNetCore.Culture=c%3Dcs-CZ%7Cuic%3Dcs-CZ";
        try (Playwright playwright = Playwright.create()) {
            Browser browser = playwright.chromium().launch();
            BrowserContext context = browser.newContext();
            Page page = context.newPage();
            page.navigate("https://portal.kermi.com/xcenterpro/cs-CZ");
            page.fill("input[name='Login']", configuration.getUsername());
            page.fill("input[name='Password']", configuration.getPassword());
            page.click("button[type='submit']");
            page.waitForURL("https://portal.kermi.com/xcenterpro/cs-CZ");
            String result =  context.cookies().stream()
                    .map(cookie -> cookie.name + "=" + cookie.value)
                    .collect(Collectors.joining("; "));
            log.info("Cookie: {}", result);
            return result;
        }
    }

    @CacheEvict(cacheNames = CACHE_NAME, allEntries = true)
    public void wipe() {
    }
}
