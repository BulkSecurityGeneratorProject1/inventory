import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { WasteDetails } from './waste-details.model';
import { WasteDetailsPopupService } from './waste-details-popup.service';
import { WasteDetailsService } from './waste-details.service';
import { ProductDetails, ProductDetailsService } from '../product-details';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-waste-details-dialog',
    templateUrl: './waste-details-dialog.component.html'
})
export class WasteDetailsDialogComponent implements OnInit {

    wasteDetails: WasteDetails;
    isSaving: boolean;

    productdetails: ProductDetails[];

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private wasteDetailsService: WasteDetailsService,
        private productDetailsService: ProductDetailsService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.productDetailsService.query()
            .subscribe((res: ResponseWrapper) => { this.productdetails = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.wasteDetails.id !== undefined) {
            this.subscribeToSaveResponse(
                this.wasteDetailsService.update(this.wasteDetails));
        } else {
            this.subscribeToSaveResponse(
                this.wasteDetailsService.create(this.wasteDetails));
        }
    }

    private subscribeToSaveResponse(result: Observable<WasteDetails>) {
        result.subscribe((res: WasteDetails) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: WasteDetails) {
        this.eventManager.broadcast({ name: 'wasteDetailsListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.jhiAlertService.error(error.message, null, null);
    }

    trackProductDetailsById(index: number, item: ProductDetails) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-waste-details-popup',
    template: ''
})
export class WasteDetailsPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private wasteDetailsPopupService: WasteDetailsPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.wasteDetailsPopupService
                    .open(WasteDetailsDialogComponent as Component, params['id']);
            } else {
                this.wasteDetailsPopupService
                    .open(WasteDetailsDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
