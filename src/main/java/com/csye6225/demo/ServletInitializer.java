/**
 * rohan magare, 001231457, magare.r@husky.neu.edu
 * ritesh gupta, 001280361, gupta.rite@husky.neu.edu
 * pratiksha shetty, 00121643697, shetty.pr@husky.neu.edu
 * yogita jain, 001643815, jain.yo@husky.neu.edu
 **/

package com.csye6225.demo;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;

public class ServletInitializer extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(DemoApplication.class);
    }

}
