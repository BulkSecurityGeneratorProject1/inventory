import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { LeftoverDetails } from './leftover-details.model';
import { LeftoverDetailsPopupService } from './leftover-details-popup.service';
import { LeftoverDetailsService } from './leftover-details.service';

@Component({
    selector: 'jhi-leftover-details-delete-dialog',
    templateUrl: './leftover-details-delete-dialog.component.html'
})
export class LeftoverDetailsDeleteDialogComponent {

    leftoverDetails: LeftoverDetails;

    constructor(
        private leftoverDetailsService: LeftoverDetailsService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.leftoverDetailsService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'leftoverDetailsListModification',
                content: 'Deleted an leftoverDetails'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-leftover-details-delete-popup',
    template: ''
})
export class LeftoverDetailsDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private leftoverDetailsPopupService: LeftoverDetailsPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.leftoverDetailsPopupService
                .open(LeftoverDetailsDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
