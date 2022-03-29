package kpu.krosno.MediPets.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    DataSource dataSource;

    @Bean
    public UserDetailsService userDetailsService() {
        return new AccountDetailsService();
    }

    @Bean
    public PasswordEncoder passwordEncoder() { return new BCryptPasswordEncoder(); }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider());
        auth
                .jdbcAuthentication()
                .dataSource(dataSource)
                .usersByUsernameQuery("select email, password, is_enabled as enabled from accounts where email=?")
                .authoritiesByUsernameQuery("select email, role from accounts, roles, accounts_roles where accounts.account_id=accounts_roles.account_id and roles.role_id=accounts_roles.role_id and email=?");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.authorizeRequests()
                .antMatchers("/logout").authenticated()
                .antMatchers("/client", "/client/*", "/account").hasAuthority("ROLE_USER")
                .antMatchers("/employee", "/employee/*").hasAnyAuthority("ROLE_SUPERUSER", "ROLE_STAFF")
                .antMatchers("/management", "/management/*").hasAnyAuthority("ROLE_SUPERUSER")
                .anyRequest().permitAll()
                .and()
                .formLogin().permitAll().loginPage("/login")
                .and()
                .logout().logoutUrl("/logout");
    }
}