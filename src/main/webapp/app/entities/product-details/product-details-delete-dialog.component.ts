import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ProductDetails } from './product-details.model';
import { ProductDetailsPopupService } from './product-details-popup.service';
import { ProductDetailsService } from './product-details.service';

@Component({
    selector: 'jhi-product-details-delete-dialog',
    templateUrl: './product-details-delete-dialog.component.html'
})
export class ProductDetailsDeleteDialogComponent {

    productDetails: ProductDetails;

    constructor(
        private productDetailsService: ProductDetailsService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.productDetailsService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'productDetailsListModification',
                content: 'Deleted an productDetails'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-product-details-delete-popup',
    template: ''
})
export class ProductDetailsDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private productDetailsPopupService: ProductDetailsPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.productDetailsPopupService
                .open(ProductDetailsDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
