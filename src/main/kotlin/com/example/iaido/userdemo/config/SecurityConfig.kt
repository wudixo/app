package com.example.iaido.userdemo.config

import com.example.iaido.userdemo.model.entity.enum.AuthRoles
import com.example.iaido.userdemo.security.AuthEntryPoint
import com.example.iaido.userdemo.security.AuthTokenFilter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
class SecurityConfig(
    val personService: UserDetailsService,
    val authTokenFilter: AuthTokenFilter,
    val authEntryPoint: AuthEntryPoint
) : WebSecurityConfigurerAdapter() {

    @Throws(Exception::class)
    override fun configure(auth: AuthenticationManagerBuilder) {
        auth.userDetailsService(personService).passwordEncoder(passwordEncoder());
    }

    @Bean
    @Primary
    public override fun authenticationManager(): AuthenticationManager {
        return super.authenticationManager()
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

    override fun configure(http: HttpSecurity) {
        http.cors().and().httpBasic().and().csrf().disable()
            .exceptionHandling().authenticationEntryPoint(authEntryPoint).and()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
            .authorizeRequests()
            .antMatchers(HttpMethod.GET, "/actuator").hasAuthority(AuthRoles.ADMIN.name)
            .antMatchers(HttpMethod.GET, "/actuator/**").hasAuthority(AuthRoles.ADMIN.name)
            .antMatchers(HttpMethod.POST, "/v1/api/auth/signup").permitAll()
            .antMatchers(HttpMethod.POST, "/v1/api/auth/authenticate").permitAll()
            .antMatchers(HttpMethod.GET, "/v1/api/person/*").hasAuthority(AuthRoles.ADMIN.name)
            .antMatchers(HttpMethod.DELETE, "/v1/api/person/remove/*").hasAuthority(AuthRoles.ADMIN.name)
            .antMatchers(HttpMethod.PATCH, "/v1/api/person/upgrade/*").hasAuthority(AuthRoles.ADMIN.name)
            .antMatchers(HttpMethod.GET, "/v1/api/person/all").hasAuthority(AuthRoles.ADMIN.name)
            .antMatchers(HttpMethod.GET, "/v1/api/person/name-search").hasAuthority(AuthRoles.ADMIN.name)
            .antMatchers(HttpMethod.GET, "/v1/api/person/shared/search")
            .hasAnyAuthority(AuthRoles.GUEST.name, AuthRoles.ADMIN.name)
            .anyRequest().denyAll()
        ;

        http.addFilterBefore(authTokenFilter, UsernamePasswordAuthenticationFilter::class.java);
    }
}
