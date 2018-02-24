/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable } from 'rxjs/Observable';
import { JhiEventManager } from 'ng-jhipster';

import { InventoryTestModule } from '../../../test.module';
import { WasteDetailsDialogComponent } from '../../../../../../main/webapp/app/entities/waste-details/waste-details-dialog.component';
import { WasteDetailsService } from '../../../../../../main/webapp/app/entities/waste-details/waste-details.service';
import { WasteDetails } from '../../../../../../main/webapp/app/entities/waste-details/waste-details.model';
import { ProductDetailsService } from '../../../../../../main/webapp/app/entities/product-details';

describe('Component Tests', () => {

    describe('WasteDetails Management Dialog Component', () => {
        let comp: WasteDetailsDialogComponent;
        let fixture: ComponentFixture<WasteDetailsDialogComponent>;
        let service: WasteDetailsService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [InventoryTestModule],
                declarations: [WasteDetailsDialogComponent],
                providers: [
                    ProductDetailsService,
                    WasteDetailsService
                ]
            })
            .overrideTemplate(WasteDetailsDialogComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(WasteDetailsDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(WasteDetailsService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('save', () => {
            it('Should call update service on save for existing entity',
                inject([],
                    fakeAsync(() => {
                        // GIVEN
                        const entity = new WasteDetails(123);
                        spyOn(service, 'update').and.returnValue(Observable.of(entity));
                        comp.wasteDetails = entity;
                        // WHEN
                        comp.save();
                        tick(); // simulate async

                        // THEN
                        expect(service.update).toHaveBeenCalledWith(entity);
                        expect(comp.isSaving).toEqual(false);
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalledWith({ name: 'wasteDetailsListModification', content: 'OK'});
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    })
                )
            );

            it('Should call create service on save for new entity',
                inject([],
                    fakeAsync(() => {
                        // GIVEN
                        const entity = new WasteDetails();
                        spyOn(service, 'create').and.returnValue(Observable.of(entity));
                        comp.wasteDetails = entity;
                        // WHEN
                        comp.save();
                        tick(); // simulate async

                        // THEN
                        expect(service.create).toHaveBeenCalledWith(entity);
                        expect(comp.isSaving).toEqual(false);
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalledWith({ name: 'wasteDetailsListModification', content: 'OK'});
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    })
                )
            );
        });
    });

});
