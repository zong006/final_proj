import Dexie, { Table } from 'dexie';
import { Section } from '../model/Section';

export class SectionDB extends Dexie{
    sections !: Table<Section, string>;

    constructor(){
        super('CityDB')
        this.version(1).stores({
            sections: 'id'
        });
        this.sections = this.table('sections')
    }

    async addSections(sections : Section[]){
        await this.sections.bulkAdd(sections);
    }

    async getSections(){
        return await this.sections.toArray();
    }
}

export const db = new SectionDB();