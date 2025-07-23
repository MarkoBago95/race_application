import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from 'src/app/core/auth.service';
import { Race, RaceService } from 'src/app/core/race.service';


@Component({
  selector: 'app-race-list',
  templateUrl:'./race-list.component.html' ,
  styleUrls: ['./race-list.component.scss']
})
export class RaceListComponent implements OnInit {
  races: Race[] = [];
  loading = true;
  showCreateForm = false;
  newRace:Partial<Race> = {};

  constructor(
    private raceService: RaceService,
    public authService: AuthService,
         private router: Router,
  ) {}

  ngOnInit() {
    this.loadRaces();
  }

  loadRaces() {
    this.loading = true;
    this.raceService.getAll().subscribe({
      next: (races) => {
        this.races = races;
        this.loading = false;
      },
      error: (error) => {
        console.error('Error loading races:', error);
        this.loading = false;
      }
    });
  }

  createRace() {
    if (this.newRace.name && this.newRace.distance) {
      this.raceService.create(this.newRace).subscribe({
        next: () => {
          this.loadRaces();
          this.cancelCreate();
        },
        error: (error) => {
          console.error('Error creating race:', error);
        }
      });
    }
  }

  cancelCreate() {
    this.showCreateForm = false;
    this.newRace = {} as Race;
  }

  applyForRace(race: Race) {
    // Navigiraj na application form za ovu trku
    this.router.navigate(['/races', race.id, 'apply']);
  }

  viewApplications(race: Race) {
    // TODO: Implementirati prikaz prijava za trku
    console.log('Viewing applications for race:', race);
  }

  deleteRace(race: Race) {
    if (confirm(`Da li ste sigurni da želite obrisati trku "${race.name}"?`)) {
      this.raceService.delete(race.id).subscribe({
        next: () => {
          this.loadRaces();
          alert('Trka je obrisana!');
        },
        error: (error) => {
          console.error('Error deleting race:', error);
          alert('Greška pri brisanju trke!');
        }
      });
    }
  }

  getDistanceLabel(distance: string): string {
    const labels = {
      'FiveK': '5K',
      'TenK': '10K',
      'HalfMarathon': 'Polumaraton',
      'Marathon': 'Maraton'
    };
    return labels[distance as keyof typeof labels] || distance;
  }

  getDistanceClass(distance: string): string {
    const classes = {
      'FiveK': 'five-k',
      'TenK': 'ten-k',
      'HalfMarathon': 'half-marathon',
      'Marathon': 'marathon'
    };
    return classes[distance as keyof typeof classes] || '';
  }
}