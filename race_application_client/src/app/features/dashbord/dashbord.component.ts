// src/app/features/dashboard/dashboard.component.ts
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { ApplicationService } from 'src/app/core/application.service';
import { AuthService } from 'src/app/core/auth.service';
import { RaceService } from 'src/app/core/race.service';
import { User } from '../model/user';


@Component({
  selector: 'app-dashboard',
  templateUrl:'./dashbord.component.html',
  styleUrls:['./dashbord.component.scss']
})
export class DashboardComponent implements OnInit {
  currentUser: User | null = null;
  totalRaces = 0;
  totalApplications = 0;
  uniqueApplicants = 0;
  favoriteDistance = 'N/A';
  recentActivities: any[] = [];

  constructor(
    private authService: AuthService,
    private raceService: RaceService,
    private applicationService: ApplicationService,
    private router: Router
  ) {}

  ngOnInit() {
    this.currentUser = this.authService.getCurrentUser();
    this.loadDashboardData();
    this.loadRecentActivities();
    console.log(this.currentUser);
    
  }

  loadDashboardData() {
    // Load races
    this.raceService.getAll().subscribe({
      next: (races) => {
        this.totalRaces = races.length;
      },
      error: (error) => console.error('Error loading races:', error)
    });

    // Load applications
    this.applicationService.getAll().subscribe({
      next: (applications) => {
        if (this.currentUser?.role === 'ADMINISTRATOR') {
          this.totalApplications = applications.length;
          this.uniqueApplicants = new Set(applications.map(app => app.firstName + app.lastName)).size;
        } else {
          // Filter user's applications (you might need to add userId to model)
          this.totalApplications = applications.length; // Placeholder
        }
      },
      error: (error) => console.error('Error loading applications:', error)
    });
  }

  loadRecentActivities() {
    // Mock recent activities - replace with real data
    this.recentActivities = [
      {
        icon: '🏃‍♂️',
        text: 'Nova trka "Zagreb Marathon" je kreirana',
        time: 'Prije 2 sata'
      },
      {
        icon: '📝',
        text: 'Nova prijava za "Plitvice Trail Run"',
        time: 'Prije 1 dan'
      },
      {
        icon: '✅',
        text: 'Prijava odobrena za "Medvednica Challenge"',
        time: 'Prije 2 dana'
      }
    ];
  }

  navigateToRaces() {
    this.router.navigate(['/races']);
  }

  navigateToApplications() {
    this.router.navigate(['/applications']);
  }

  createNewRace() {
    this.router.navigate(['/races'], { queryParams: { action: 'create' } });
  }

  findRecommendedRaces() {
    this.router.navigate(['/races'], { queryParams: { filter: 'recommended' } });
  }
}