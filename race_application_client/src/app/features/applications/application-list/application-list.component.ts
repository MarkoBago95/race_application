import { Component, OnInit } from '@angular/core';
import {Application, ApplicationService } from 'src/app/core/application.service';


@Component({
  selector: 'app-application-list',
  templateUrl: './application-list.component.html',
  styleUrls: ['./application-list.component.scss']
})
export class ApplicationListComponent implements OnInit {
  applications: Application[] = [];

  constructor(
    private service: ApplicationService  ) {}

  ngOnInit(): void {
    this.service.getAll().subscribe(data => this.applications = data);
  }

  delete(id: string) {
    this.service.delete(id).subscribe(() => {
      this.applications = this.applications.filter(app => app.id !== id);
    });
  }
}