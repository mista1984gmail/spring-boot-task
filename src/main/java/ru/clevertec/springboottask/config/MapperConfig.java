package ru.clevertec.springboottask.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.clevertec.springboottask.mapper.HouseHistoryMapper;
import ru.clevertec.springboottask.mapper.HouseHistoryMapperImpl;
import ru.clevertec.springboottask.mapper.HouseMapper;
import ru.clevertec.springboottask.mapper.HouseMapperImpl;
import ru.clevertec.springboottask.mapper.PersonMapper;
import ru.clevertec.springboottask.mapper.PersonMapperImpl;

@Configuration
public class MapperConfig {

    @Bean
    public PersonMapper personMapper (){
        return new PersonMapperImpl();
    }

    @Bean
    public HouseMapper houseMapper (){
        return new HouseMapperImpl();
    }

    @Bean
    public HouseHistoryMapper houseHistoryMapper (){
        return new HouseHistoryMapperImpl();
    }
}
