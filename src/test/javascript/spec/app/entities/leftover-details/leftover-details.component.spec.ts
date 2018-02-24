/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { Headers } from '@angular/http';

import { InventoryTestModule } from '../../../test.module';
import { LeftoverDetailsComponent } from '../../../../../../main/webapp/app/entities/leftover-details/leftover-details.component';
import { LeftoverDetailsService } from '../../../../../../main/webapp/app/entities/leftover-details/leftover-details.service';
import { LeftoverDetails } from '../../../../../../main/webapp/app/entities/leftover-details/leftover-details.model';

describe('Component Tests', () => {

    describe('LeftoverDetails Management Component', () => {
        let comp: LeftoverDetailsComponent;
        let fixture: ComponentFixture<LeftoverDetailsComponent>;
        let service: LeftoverDetailsService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [InventoryTestModule],
                declarations: [LeftoverDetailsComponent],
                providers: [
                    LeftoverDetailsService
                ]
            })
            .overrideTemplate(LeftoverDetailsComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(LeftoverDetailsComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(LeftoverDetailsService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new Headers();
                headers.append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(Observable.of({
                    json: [new LeftoverDetails(123)],
                    headers
                }));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.leftoverDetails[0]).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
