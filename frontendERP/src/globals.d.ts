import type * as React from 'react';

declare global {
  namespace JSX {
    type Element = React.JSX.Element;
    type ElementType = React.JSX.ElementType;
    type IntrinsicAttributes = React.JSX.IntrinsicAttributes;
    interface IntrinsicClassAttributes<T> extends React.JSX.IntrinsicClassAttributes<T> {}
  }
}

export {};

