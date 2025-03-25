import { inject, Injectable } from '@angular/core';
import 'firebase/auth';
import { Router } from '@angular/router';
import { AngularFireAuth } from '@angular/fire/compat/auth';
import { getAuth, GoogleAuthProvider, signInWithPopup, signOut } from 'firebase/auth';


import { LoginStore } from '../../store/LoginStore';
import { User } from '../../model/User';
import { UserService } from '../user/user.service';
import { ArticleStore } from '../../store/ArticleStore';
import { firstValueFrom } from 'rxjs';


@Injectable({
  providedIn: 'root'
})
export class LoginService {

  user : User = {
    id : '',
    username : '',
    displayName : '',
    selectedTopics : []
  };

  constructor(private afAuth: AngularFireAuth, private router: Router) { }

  private loginStore = inject(LoginStore);
  private userService = inject(UserService);
  private articleStore = inject(ArticleStore);

  async loginWithGoogle() {
    try {
      const auth = getAuth();
      const provider = new GoogleAuthProvider();
      
      const result = await signInWithPopup(auth, provider);

      console.log('>>>logged in: ', result.user);  
      
      const user = result.user;

        if (user) {
          console.info('>>> logged in state after authState: ', user.email);
          

          const loggedinUser : User = {
            id : '',
            username : user.email as string,
            displayName : user.displayName as string,
            selectedTopics : []
          }

          const idToken = await user.getIdToken(true);
          console.info('>>> fb token: ', idToken);
          sessionStorage.setItem('idToken' , idToken); 

          const response = await firstValueFrom(this.userService.loginUser(loggedinUser));
          loggedinUser.id = response.id;
          loggedinUser.selectedTopics = response.selectedTopics;
          this.loginStore.setUser$(loggedinUser);
          this.loginStore.setLoggedIn$(true);

          
          console.info('>>> login: ', loggedinUser.id)
          if (loggedinUser.id) {
            this.router.navigate(['/dashboard']);
          }

          // this.router.navigate(['/dashboard']);
        } else {
          console.info('>>> logged out state after authState');
          
          this.loginStore.setLoggedIn$(false);
        }
      
    } catch (error) {
      console.error("Login failed:", error);
    }
    
  }

  async logout() {
    try {
      const auth = getAuth();  // Initialize Auth instance
      await signOut(auth);  // Perform sign-out
      sessionStorage.removeItem('idToken');
      this.loginStore.setLoggedIn$(false);
      this.loginStore.resetDoomScrollStoreDelta$(0)
      this.articleStore.clear();
      
      this.router.navigate(['/']);  // Redirect to the homepage after logout
    } catch (error) {
      console.error("Logout failed:", error);  // Handle logout failure
    }
  }
}
