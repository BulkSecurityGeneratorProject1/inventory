import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { SalesDetails } from './sales-details.model';
import { SalesDetailsPopupService } from './sales-details-popup.service';
import { SalesDetailsService } from './sales-details.service';

@Component({
    selector: 'jhi-sales-details-delete-dialog',
    templateUrl: './sales-details-delete-dialog.component.html'
})
export class SalesDetailsDeleteDialogComponent {

    salesDetails: SalesDetails;

    constructor(
        private salesDetailsService: SalesDetailsService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.salesDetailsService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'salesDetailsListModification',
                content: 'Deleted an salesDetails'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-sales-details-delete-popup',
    template: ''
})
export class SalesDetailsDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private salesDetailsPopupService: SalesDetailsPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.salesDetailsPopupService
                .open(SalesDetailsDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
