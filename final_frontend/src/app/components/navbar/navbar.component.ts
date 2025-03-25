import { Component, inject } from '@angular/core';
import { LoginStore } from '../../store/LoginStore';
import { LoginService } from '../../service/login/login.service';
import { map } from 'rxjs';
import { ArticleStore } from '../../store/ArticleStore';

@Component({
  selector: 'app-navbar',
  standalone: false,
  templateUrl: './navbar.component.html',
  styleUrl: './navbar.component.css'
})
export class NavbarComponent {
  private loginStore = inject(LoginStore);
  private loginSvc = inject(LoginService);
  private articleStore = inject(ArticleStore);

  username = this.loginStore.user$.pipe(map(u => u.displayName));
  isLoggedIn = this.loginStore.loggedIn$

  logout(){
    this.loginSvc.logout();
    this.articleStore.clear();
  }

  login(){
    this.loginSvc.loginWithGoogle();
  }

}
