import PropTypes from 'prop-types';
export declare const ordinalColorsPropType: PropTypes.Requireable<string | ((...args: any[]) => any) | (string | null)[] | PropTypes.InferProps<{
    scheme: PropTypes.Validator<import("./schemes").ColorSchemeId>;
    size: PropTypes.Requireable<number>;
}> | PropTypes.InferProps<{
    datum: PropTypes.Validator<string>;
}>>;
export declare const colorPropertyAccessorPropType: PropTypes.Requireable<string | ((...args: any[]) => any)>;
export declare const inheritedColorPropType: PropTypes.Requireable<string | ((...args: any[]) => any) | PropTypes.InferProps<{
    theme: PropTypes.Validator<string>;
}> | PropTypes.InferProps<{
    from: PropTypes.Validator<string>;
    modifiers: PropTypes.Requireable<(any[] | null)[]>;
}>>;
//# sourceMappingURL=props.d.ts.map