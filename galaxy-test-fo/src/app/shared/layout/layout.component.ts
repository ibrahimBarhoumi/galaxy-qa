import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { KeycloakService } from 'keycloak-angular';
import { KeycloakLoginOptions } from 'keycloak-js';
import { CookieService } from 'ngx-cookie-service';
import { navItems } from '../../_nav';
import {LanguageService} from '../services/language.service';

@Component({
  selector: 'app-container',
  templateUrl: './layout.component.html',
})
export class LayoutComponent implements OnInit {
  public navItems = navItems;
  selectLanguage: any;
  cookiesValue
  changedValue
  isLoggedIn = true

  listLangs = [
    { text: 'English', class: 'flag-icon-us', lang: 'en' },
    { text: 'French', class: 'flag-icon-fr', lang: 'fr' },
    { text: 'Spanish', class: 'flag-icon-es', lang: 'es' },
  ];

  constructor(
    public languageService: LanguageService,
    private keycloakService: KeycloakService,
    private cookieService: CookieService
  ) {}
  ngOnInit(): void {
    // todo create userdetail object
    this.cookiesValue = this.cookieService.getAll()
    this.changedValue = this.cookieService.get('lang')

  if( Object.entries(this.cookiesValue).length === 0 ){
    this.keycloakService.loadUserProfile().then((data) => {
      this.selectLanguage = this.listLangs.find(element => element.lang ===  data['attributes']['locale'][0]);
      this.languageService.setLanguage(this.selectLanguage.lang);
      console.log(this.selectLanguage.lang)
      });
    }else{
      this.selectLanguage = this.listLangs.find(element => element.lang ===  this.changedValue)
    }
  
  }

  logout() {
    this.keycloakService.logout();
    this.cookieService.deleteAll()
  }

  changeLanguage(lang: any) {
    this.selectLanguage = lang;
    this.languageService.setLanguage(this.selectLanguage.lang);
  }
}
