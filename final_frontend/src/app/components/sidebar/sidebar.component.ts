import { Component, inject, OnInit } from '@angular/core';
import { LoginStore } from '../../store/LoginStore';
import { DashboardService } from '../../service/dashboard/dashboard.service';
import { DomSanitizer, SafeUrl } from '@angular/platform-browser';
import { Observable } from 'rxjs';
import { Router } from '@angular/router';

@Component({
  selector: 'app-sidebar',
  standalone: false,
  templateUrl: './sidebar.component.html',
  styleUrl: './sidebar.component.css'
})
export class SidebarComponent implements OnInit{
  ngOnInit(): void {
    this.setImage();
    this.loggedIn = this.loginStore.loggedIn$
    this.loginStore.user$.subscribe(
      (u) => {
        console.info('>>>sidebar: ', u)
        this.dashboardService.getQrCode(u).subscribe(
          (blob) => {
            const objectURL = URL.createObjectURL(blob);
            this.qrCodeUrl = this.sanitizer.bypassSecurityTrustUrl(objectURL);
          }
        )
      }
    )
  }

  private loginStore = inject (LoginStore);
  private dashboardService = inject(DashboardService);
  private router = inject(Router);
  private sanitizer = inject(DomSanitizer);
  qrCodeUrl: SafeUrl | null = null;

  imgToUse = '';
  delta = this.loginStore.delta$;
  loggedIn !: Observable<boolean>
  died = this.loginStore.died$;

  setImage(){
    this.loginStore.delta$.subscribe(
      (score) => {
        if (score<25 && score >=0){
          
          this.imgToUse = 'assets/images/cat_stage1.gif';
        }
        else if (score >= 25 && score < 50){
          
          this.imgToUse = 'assets/images/cat_stage2.gif';
        }
        else if (score >=50 && score < 75){
          
          this.imgToUse = 'assets/images/cat_stage3_5.gif';
        }
        else if (score >=75 && score < 100){
          
          this.imgToUse = 'assets/images/cat_stage4.gif';
        }
        else if (score >= 100){
          // cat died
          
          this.loginStore.setDied$(true);
          this.imgToUse = 'assets/images/cat_stage3_5.gif'

          let toSerenity = confirm('That is too much. Go calm yourself down...')

          if (toSerenity){
            // console.info

            // this.loginStore.setDied$(false);
          }
          else{
            // set disabled flag = true, prevent fetching more articles
            alert('Too sad to continue. No more articles until you calm yourself down.')
          }

        }
      }
    )
  }


  getQrCode(){
    this.loginStore.user$.subscribe(
      (u) => {
        this.dashboardService.getQrCode(u)
      }
    )
  }

  toCalm(x : string){
    console.info('>>>to calm method: ', x)
    this.router.navigate(['/calm', x])
  }
}
