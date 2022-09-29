package ru.job4j.cinema.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    private final DataSource ds;
    private static final String SELECT_USER_QUERY = """
            SELECT 
                username, 
                password, 
                enabled 
            FROM 
                users
            WHERE 
                username = ?
            """;

    private static final String SELECT_AUTHORITY_BY_USERNAME_QUERY = """
            SELECT 
                u.username, 
                a.authority
            FROM 
                authorities as a, 
                users as u
            WHERE 
                u.username = ? 
                AND u.authority_id = a.id
            """;


    public WebSecurityConfig(DataSource ds) {
        this.ds = ds;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication().dataSource(ds)
                .usersByUsernameQuery(SELECT_USER_QUERY)
                .authoritiesByUsernameQuery(SELECT_AUTHORITY_BY_USERNAME_QUERY);
    }

    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/login", "/reg", "/movies-today", "/movies-today/poster/**", "/")
                .permitAll()
                .antMatchers("/movies-today/**", "/profile/**", "/css/**", "/js/**")
                .hasAnyRole("USER", "ADMIN")
                .antMatchers("/**")
                .hasRole("ADMIN")
                .and()
                .formLogin()
                .loginPage("/login")
                .defaultSuccessUrl("/")
                .failureUrl("/login?error=true")
                .permitAll()
                .and()
                .logout()
                .logoutSuccessUrl("/login?logout=true")
                .invalidateHttpSession(true)
                .permitAll()
                .and()
                .csrf()
                .disable();
    }

}
