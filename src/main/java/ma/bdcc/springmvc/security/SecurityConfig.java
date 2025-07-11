package ma.bdcc.springmvc.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

@Slf4j
@Configuration
public class SecurityConfig {

    /*@Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // 1) which URLs are public / protected
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/webjars/**",
                                "/css/**", "/js/**",
                                "/h2-console/**")
                        .permitAll()
                        .anyRequest().authenticated()
                )
                // 2) enable form-login with default login page

                .formLogin(Customizer.withDefaults())
                // 3) logout support
                .logout(logout -> logout
                        .logoutSuccessUrl("/login?logout")
                )
                // 4) H2 console needs frameOptions disabled
                //.headers(headers -> headers.frameOptions().sameOrigin())
                .headers(headers -> headers
                        .defaultsDisabled()
                        .contentSecurityPolicy(csp -> csp
                                .policyDirectives("default-src 'self'; " +
                                        "frame-ancestors 'self'; " +
                                        "form-action 'self'; " +
                                        "script-src 'self'")
                        )
                        .cacheControl(Customizer.withDefaults())
                        .httpStrictTransportSecurity(Customizer.withDefaults())
                )
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers("/h2-console/**")
                )
                .exceptionHandling(exception -> exception.accessDeniedPage("/unauthorized"));

        return http.build();
    }*/


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        // public assets & login
                        .requestMatchers(
                                "/webjars/**", "/css/**", "/js/**",
                                "/h2-console/**", "/login", "/error"
                        ).permitAll()

                        // GET /products/** → USER or ADMIN
                        .requestMatchers(HttpMethod.GET, "/products/all")
                        .hasAnyRole("USER","ADMIN")
                        // POST, PUT, DELETE /products/** → ADMIN only
                        .requestMatchers( "/products/admin/**")
                        .hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .formLogin(Customizer.withDefaults())
                /*.logout(logout -> logout
                        .logoutSuccessUrl("/login?logout")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                )*/
                .headers(headers -> headers
                        .defaultsDisabled()
                        .contentSecurityPolicy(csp -> csp
                                .policyDirectives("default-src 'self'; " +
                                        "frame-ancestors 'self'; " +
                                        "form-action 'self'; " +
                                        "script-src 'self' 'unsafe-inline'")
                        )
                        .cacheControl(Customizer.withDefaults())
                        .httpStrictTransportSecurity(Customizer.withDefaults())
                )
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers("/h2-console/**")
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                        .maximumSessions(1)
                )
                .exceptionHandling(exception -> exception
                        .accessDeniedPage("/unauthorized")
                );

        return http.build();
    }

// for h2
    /*@Bean
    public UserDetailsService users(PasswordEncoder encoder) {
        var user = User.withUsername("user")
                .password(encoder.encode("password"))
                .roles("USER")
                .build();
        return new InMemoryUserDetailsManager(user);
    }*/


    @Bean
    public JdbcUserDetailsManager users(DataSource dataSource, PasswordEncoder encoder) {
        var userDetailsManager = new JdbcUserDetailsManager(dataSource);

        // create USER account if not present
        if (!userDetailsManager.userExists("user")) {
            userDetailsManager.createUser(User.withUsername("user")
                    .password(encoder.encode("password"))
                    .roles("USER")
                    .build());
        }
        // create ADMIN account if not present
        if (!userDetailsManager.userExists("admin")) {
            userDetailsManager.createUser(User.withUsername("admin")
                    .password(encoder.encode("adminpass"))
                    .roles("ADMIN")
                    .build());
        }
        return userDetailsManager;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
