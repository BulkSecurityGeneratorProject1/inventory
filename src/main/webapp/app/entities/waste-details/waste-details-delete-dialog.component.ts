import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { WasteDetails } from './waste-details.model';
import { WasteDetailsPopupService } from './waste-details-popup.service';
import { WasteDetailsService } from './waste-details.service';

@Component({
    selector: 'jhi-waste-details-delete-dialog',
    templateUrl: './waste-details-delete-dialog.component.html'
})
export class WasteDetailsDeleteDialogComponent {

    wasteDetails: WasteDetails;

    constructor(
        private wasteDetailsService: WasteDetailsService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.wasteDetailsService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'wasteDetailsListModification',
                content: 'Deleted an wasteDetails'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-waste-details-delete-popup',
    template: ''
})
export class WasteDetailsDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private wasteDetailsPopupService: WasteDetailsPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.wasteDetailsPopupService
                .open(WasteDetailsDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
