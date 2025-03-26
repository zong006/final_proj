import { Component, inject, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { LoginStore } from '../../store/LoginStore';

@Component({
  selector: 'app-calm',
  standalone: false,
  templateUrl: './calm.component.html',
  styleUrl: './calm.component.css'
})
export class CalmComponent implements OnInit, OnDestroy{
  ngOnDestroy(): void {
    this.loginStore.setDied$(false);
    this.loginStore.resetDoomScrollStoreDelta$(0)
  }

  ngOnInit(): void {
    this.img = this.activatedRoute.snapshot.params['image'];
    console.info('>>> image: ', this.img)
    this.imgPath = 'assets/images/'+this.img + '.jpg';
    console.info('>>>image path: ', this.imgPath)
  }

  private activatedRoute = inject(ActivatedRoute);
  private loginStore = inject(LoginStore);
  img = '';
  imgPath = '';
  private router = inject(Router);


  goToFeed(){
    this.router.navigate(['dashboard'])
  }
}
