import { BrowserModule } from '@angular/platform-browser';
import { NgModule, APP_INITIALIZER } from '@angular/core';
import { LocationStrategy, HashLocationStrategy, CommonModule } from '@angular/common';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import {HttpClient, HttpClientModule} from '@angular/common/http';
import { PerfectScrollbarModule } from 'ngx-perfect-scrollbar';
import { PerfectScrollbarConfigInterface } from 'ngx-perfect-scrollbar';
import {DragDropModule} from '@angular/cdk/drag-drop';

const DEFAULT_PERFECT_SCROLLBAR_CONFIG: PerfectScrollbarConfigInterface = {
  suppressScrollX: true
};

import { AppComponent } from './app.component';

// Import containers
import { LayoutComponent } from './shared/layout/layout.component';

import { LoginComponent } from './views/login/login.component';

import {
  AppBreadcrumbModule,
  AppHeaderModule,
  AppSidebarModule,
} from '@coreui/angular';

// Import routing module
import { AppRoutingModule } from './app.routing';

// Import 3rd party components
import { BsDropdownModule } from 'ngx-bootstrap/dropdown';
import { KeycloakAngularModule, KeycloakService } from 'keycloak-angular';
import { initializer } from './utils/initializer';
import { EnvironmentModule } from './views/environment/environment.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { ButtonsModule } from 'ngx-bootstrap/buttons';
import {CampaignModule, createTranslateLoader} from './views/campaign/campaign.module';
import { PopoverModule } from 'ngx-bootstrap/popover';
import {TranslateLoader, TranslateModule} from '@ngx-translate/core';
import { NgSelectModule } from '@ng-select/ng-select';
import { ApplicationModule } from './views/application/application.module';
import { FunctionalConfigModule } from './views/functionalconfig/functionalconfig.module';
import { TestModule } from './views/test/test.module';
import { DataTestCaseModule } from './views/datatestcase/datatestcase.module';

@NgModule({
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    DragDropModule,
    AppRoutingModule,
    AppBreadcrumbModule.forRoot(),
    AppHeaderModule,
    AppSidebarModule,
    PerfectScrollbarModule,
    BsDropdownModule.forRoot(),
    HttpClientModule,
    KeycloakAngularModule,
    EnvironmentModule,
    PopoverModule.forRoot(),
    ButtonsModule.forRoot(),
    EnvironmentModule,
    CampaignModule,
    ApplicationModule,
    TestModule,
    FunctionalConfigModule,
    NgSelectModule,
    DataTestCaseModule,
    TranslateModule.forRoot({
      loader: {
        provide: TranslateLoader,
        useFactory: createTranslateLoader,
        deps: [HttpClient],
      },
    }),
  ],
  declarations: [
    AppComponent,
    LayoutComponent,
    LoginComponent,

  ],
  providers: [
    {
      provide: LocationStrategy,
      useClass: HashLocationStrategy
    },
    {
      provide: APP_INITIALIZER,
      useFactory: initializer,
      deps: [ KeycloakService ],
      multi: true
    }
  ],
  bootstrap: [ AppComponent ]
})
export class AppModule { }
