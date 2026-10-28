package com.indra.catalog.products.infrastructure.security;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http)throws Exception {

        return http.csrf(csrf -> csrf.disable()).authorizeHttpRequests(auth -> auth.requestMatchers(org.springframework.http.HttpMethod.GET,"/api/products/*").permitAll()
                        .requestMatchers(org.springframework.http.HttpMethod.POST, "/api/products").hasAnyRole("EDITOR","ADMIN")
                        .requestMatchers(org.springframework.http.HttpMethod.DELETE, "/api/products/*")
                        .hasRole("ADMIN").anyRequest().authenticated()).httpBasic(Customizer.withDefaults()).build();
                        
    }

    @Bean
    public InMemoryUserDetailsManager users(
        @Value("${app.security.editor.username}")
        String editorUser,

        @Value("${app.security.editor.password}")
        String editorPassword,

        @Value("${app.security.admin.username}")
        String adminUser,

        @Value("${app.security.admin.password}")
        String adminPassword) {

        UserDetails editor = User.withUsername(editorUser).password("{noop}" + editorPassword).roles("EDITOR").build();

        UserDetails admin = User.withUsername(adminUser).password("{noop}" + adminPassword).roles( "EDITOR","ADMIN").build();

        return new InMemoryUserDetailsManager(editor,admin);
    }

}
