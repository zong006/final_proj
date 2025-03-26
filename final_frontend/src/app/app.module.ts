import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { AngularFireModule } from '@angular/fire/compat';
import { AngularFireAuthModule } from '@angular/fire/compat/auth';
import { environment } from '../environment/Environment';
import { initializeApp } from 'firebase/app';
import { InfiniteScrollDirective } from 'ngx-infinite-scroll';


import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { LoginComponent } from './components/login/login.component';
import { DashboardComponent } from './components/dashboard/dashboard.component';
import { NavbarComponent } from './components/navbar/navbar.component';
import { UserSettingsComponent } from './components/user-settings/user-settings.component';
import { ReactiveFormsModule } from '@angular/forms';
import { provideHttpClient } from '@angular/common/http';
import { SidebarComponent } from './components/sidebar/sidebar.component';
import { LeaderboardComponent } from './components/leaderboard/leaderboard.component';
import { CalmComponent } from './components/calm/calm.component';


@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    DashboardComponent,
    NavbarComponent,
    UserSettingsComponent,
    SidebarComponent,
    LeaderboardComponent,
    CalmComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    AngularFireModule.initializeApp(environment.firebaseConfig), //<---- removed these two lines for now. used it in the constructor below
    AngularFireAuthModule,
    ReactiveFormsModule,
    InfiniteScrollDirective
  ],
  providers: [provideHttpClient()],
  bootstrap: [AppComponent]
})
export class AppModule { 
  constructor(){
    initializeApp(environment.firebaseConfig);
  }
  
}
