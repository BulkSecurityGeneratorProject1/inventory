/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { Headers } from '@angular/http';

import { InventoryTestModule } from '../../../test.module';
import { WasteDetailsComponent } from '../../../../../../main/webapp/app/entities/waste-details/waste-details.component';
import { WasteDetailsService } from '../../../../../../main/webapp/app/entities/waste-details/waste-details.service';
import { WasteDetails } from '../../../../../../main/webapp/app/entities/waste-details/waste-details.model';

describe('Component Tests', () => {

    describe('WasteDetails Management Component', () => {
        let comp: WasteDetailsComponent;
        let fixture: ComponentFixture<WasteDetailsComponent>;
        let service: WasteDetailsService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [InventoryTestModule],
                declarations: [WasteDetailsComponent],
                providers: [
                    WasteDetailsService
                ]
            })
            .overrideTemplate(WasteDetailsComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(WasteDetailsComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(WasteDetailsService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new Headers();
                headers.append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(Observable.of({
                    json: [new WasteDetails(123)],
                    headers
                }));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.wasteDetails[0]).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
