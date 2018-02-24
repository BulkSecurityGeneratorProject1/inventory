import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager } from 'ng-jhipster';

import { WasteDetails } from './waste-details.model';
import { WasteDetailsService } from './waste-details.service';

@Component({
    selector: 'jhi-waste-details-detail',
    templateUrl: './waste-details-detail.component.html'
})
export class WasteDetailsDetailComponent implements OnInit, OnDestroy {

    wasteDetails: WasteDetails;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private wasteDetailsService: WasteDetailsService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInWasteDetails();
    }

    load(id) {
        this.wasteDetailsService.find(id).subscribe((wasteDetails) => {
            this.wasteDetails = wasteDetails;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInWasteDetails() {
        this.eventSubscriber = this.eventManager.subscribe(
            'wasteDetailsListModification',
            (response) => this.load(this.wasteDetails.id)
        );
    }
}
