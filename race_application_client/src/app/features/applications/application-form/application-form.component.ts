import { Component, Input } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { ApplicationService } from 'src/app/core/application.service';
import { Race, RaceService } from 'src/app/core/race.service';

@Component({
  selector: 'app-application-form',
  templateUrl:'./application-form.component.html' ,
  styleUrls: ['./application-form.component.scss']
})
export class ApplicationFormComponent {
 applicationForm: FormGroup;
  selectedRace: Race | null = null;
  raceId: string | null = null;
  isSubmitting = false;

  constructor(
    private fb: FormBuilder,
    private route: ActivatedRoute,
    private router: Router,
    private applicationService: ApplicationService,
    private raceService: RaceService
  ) {
    this.applicationForm = this.fb.group({
      firstName: ['', [Validators.required, Validators.maxLength(255)]],
      lastName: ['', [Validators.required, Validators.maxLength(255)]],
      club: ['', [Validators.maxLength(255)]]
    });
  }

  ngOnInit() {
    this.raceId = this.route.snapshot.paramMap.get('raceId');
    if (this.raceId) {
      this.loadRace(this.raceId);
    }
  }

  loadRace(raceId: string) {
    this.raceService.getOne(raceId).subscribe({
      next: (race) => {
        this.selectedRace = race;
      },
      error: (error) => {
        console.error('Error loading race:', error);
        alert('Greška pri učitavanju trke!');
        this.goBack();
      }
    });
  }

  onSubmit() {
    if (this.applicationForm.valid && this.raceId) {
      this.isSubmitting = true;
      
      const applicationData = {
        ...this.applicationForm.value,
        raceId: this.raceId
      };

      this.applicationService.create(applicationData).subscribe({
        next: (application) => {
          alert('Prijava je uspješno poslana!');
          this.router.navigate(['/applications']);
        },
        error: (error) => {
          console.error('Error creating application:', error);
          alert('Greška pri slanju prijave!');
          this.isSubmitting = false;
        }
      });
    }
  }

  goBack() {
    this.router.navigate(['/races']);
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
}
